package com.ahead.airbnb.functions;

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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

/**
 * Configuration class for National Debt related functions.
 * */
@Configuration
public class NationalDebtFunctions {

	private static final Logger log = LoggerFactory.getLogger(NationalDebtFunctions.class);

	/**
	 * Bean that defines a function to fetch and sum the national debt from a given API.
	 *
	 * @param webClientFromBuilder the WebClient.Builder to build the WebClient
	 * @return a Function that takes a Mono<String> and returns a Mono<NationalDebtRecord>
	 */
	@Bean
	public Function<Mono<String>, Mono<NationalDebtRecord>> debtSumFunction(WebClient.Builder webClientFromBuilder) {
		LocalDate lastBusinessDay = BusinessDayCalculator.getPreviousUSGovernmentBusinessDay(LocalDate.now());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = Date.from(lastBusinessDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
		String url = String.format("https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/dts/debt_subject_to_limit?filter=record_date:gte:%s&fields=record_date,debt_catg,close_today_bal", sdf.format(date));
		return request -> webClientFromBuilder.build().get()
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
						ObjectMapper mapper = new ObjectMapper();
						JsonNode root = mapper.readTree(response);
						for (JsonNode data : root.get("data")) {
							DebtRecord record = objectMapper.readValue(data.toString(), DebtRecord.class);
							sum += Long.parseLong(record.close_today_bal());
						}
					} catch (Exception e) {
						return Mono.error(new RuntimeException("Error parsing JSON response", e));
					}
					return Mono.just(new NationalDebtRecord(sum));
				});
	}

}
