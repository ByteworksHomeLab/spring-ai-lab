package com.ahead.airbnb.functions;

import com.ahead.airbnb.records.NationalDebtRecord;
import com.ahead.airbnb.services.NationalDebtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NationalDebtServiceTest {

    @Autowired
    private NationalDebtService nationalDebtService;

    @MockBean
    private WebClient.Builder webClientBuilder;

    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    public void setUp() {
        WebClient webClient = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(Mockito.any(), Mockito.any())).thenReturn(responseSpec);

        // Inject the mocked WebClient into the NationalDebtService instance
        nationalDebtService = new NationalDebtService(webClientBuilder);
    }

    @Test
    public void getNationalDebtRecordReturnsNationalDebtRecordOnValidResponse() {
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(getMockJsonResponse()));

        Mono<NationalDebtRecord> result = nationalDebtService.apply("2024-08-23");

        StepVerifier.create(result).expectNextMatches(record -> record.publicDebtInMillions() == 35339820L).verifyComplete();
    }

    @Test
    public void getNationalDebtRecordHandlesEmptyResponse() {
        String jsonResponse = "{\"data\":[]}";
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(jsonResponse));

        Mono<NationalDebtRecord> result = nationalDebtService.apply("2024-08-23");

        StepVerifier.create(result).expectNextMatches(record -> record.publicDebtInMillions() == 0).verifyComplete();
    }

    @Test
    public void getNationalDebtRecordHandlesInvalidJsonResponse() {
        String jsonResponse = "invalid json";
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(jsonResponse));

        Mono<NationalDebtRecord> result = nationalDebtService.apply("2024-08-23");

        StepVerifier.create(result).expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().contains("Error parsing JSON response")).verify();
    }

    @Test
    public void getNationalDebtRecordHandlesClientError() {
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new RuntimeException("API request failed with status: 400")));

        Mono<NationalDebtRecord> result = nationalDebtService.apply("2024-08-23");

        StepVerifier.create(result).expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().contains("API request failed with status: 400")).verify();
    }

    public String getMockJsonResponse() {
        return "{\"data\":[{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Debt Held by the Public\",\"close_today_bal\":\"28066894\"},{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Intragovernmental Holdings\",\"close_today_bal\":\"7158290\"},{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Debt Not Subject to Limit\",\"close_today_bal\":\"476\"},{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Debt Not Subject to Limit\",\"close_today_bal\":\"109646\"},{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Debt Not Subject to Limit\",\"close_today_bal\":\"4514\"},{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Other Debt Subject to Limit\",\"close_today_bal\":\"0\"},{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Statutory Debt Limit\",\"close_today_bal\":\"0\"}],\"meta\":{\"count\":7,\"labels\":{\"record_date\":\"Record Date\",\"debt_catg\":\"Debt Category\",\"close_today_bal\":\"Closing Balance Today\"},\"dataTypes\":{\"record_date\":\"DATE\",\"debt_catg\":\"STRING\",\"close_today_bal\":\"CURRENCY0\"},\"dataFormats\":{\"record_date\":\"YYYY-MM-DD\",\"debt_catg\":\"String\",\"close_today_bal\":\"$1,000,000\"},\"total-count\":7,\"total-pages\":1},\"links\":{\"self\":\"&page%5Bnumber%5D=1&page%5Bsize%5D=100\",\"first\":\"&page%5Bnumber%5D=1&page%5Bsize%5D=100\",\"prev\":null,\"next\":null,\"last\":\"&page%5Bnumber%5D=1&page%5Bsize%5D=100\"}}";
    }
}