package com.ahead.airbnb.services;

import com.ahead.airbnb.functions.BusinessDayCalculator;
import com.ahead.airbnb.records.DebtRecord;
import com.ahead.airbnb.records.NationalDebtRecord;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.function.Function;

@Service
public class NationalDebtService implements Function<String, Mono<NationalDebtRecord>> {

    private static final Logger log = LoggerFactory.getLogger(NationalDebtService.class);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private final WebClient webClient;

    public NationalDebtService(WebClient.Builder webClientFromBuilder) {
        this.webClient = webClientFromBuilder.build();
    }

    @Override
    public Mono<NationalDebtRecord> apply(String currentDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale( Locale.US );
        LocalDate lastBusinessDay = BusinessDayCalculator.getPreviousUSGovernmentBusinessDay(LocalDate.parse(currentDate, formatter));
        Date date = Date.from(lastBusinessDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        String url = String.format("https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/dts/debt_subject_to_limit?filter=record_date:eq:%s&fields=record_date,debt_catg,close_today_bal", sdf.format(date));

        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new RuntimeException("API request failed with status: " + clientResponse.statusCode())))
                .bodyToMono(String.class)
                .flatMap(response -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    long sum = 0;
                    try {
                        JsonNode root = objectMapper.readTree(response);
                        for (JsonNode data : root.get("data")) {
                            DebtRecord record = objectMapper.readValue(data.toString(), DebtRecord.class);
                            sum += Long.parseLong(record.close_today_bal());
                        }
                    } catch (Exception e) {
                        log.error("Error parsing JSON response", e);
                        return Mono.error(new RuntimeException("Error parsing JSON response", e));
                    }
                    return Mono.just(new NationalDebtRecord(sum));
                });
    }
}
