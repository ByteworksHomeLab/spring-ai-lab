package com.ahead.airbnb.functions;

import com.ahead.airbnb.records.NationalDebtRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.function.Function;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebtSumFunctionTest {

    @Test
    void testDebtSumFunction() {
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        WebClient webClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(getMockJsonResponse()));

        NationalDebtFunctions cloudFunctions = new NationalDebtFunctions();
        Function<Mono<String>, Mono<NationalDebtRecord>> debtSumFunction = cloudFunctions.debtSumFunction(webClientBuilder);
        LocalDate businessDay = LocalDate.of(2024, 8, 26);
        NationalDebtRecord nationalDebt = new NationalDebtRecord(35339820L);
        StepVerifier.create(debtSumFunction.apply(Mono.just("")))
                .expectNext(nationalDebt)
                .verifyComplete();
    }

    private String getMockJsonResponse() {
        return "{\"data\":[{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Debt Held by the Public\",\"close_today_bal\":\"28066894\"},{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Intragovernmental Holdings\",\"close_today_bal\":\"7158290\"},{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Debt Not Subject to Limit\",\"close_today_bal\":\"476\"},{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Debt Not Subject to Limit\",\"close_today_bal\":\"109646\"},{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Debt Not Subject to Limit\",\"close_today_bal\":\"4514\"},{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Other Debt Subject to Limit\",\"close_today_bal\":\"0\"},{\"record_date\":\"2024-08-26\",\"debt_catg\":\"Statutory Debt Limit\",\"close_today_bal\":\"0\"}],\"meta\":{\"count\":7,\"labels\":{\"record_date\":\"Record Date\",\"debt_catg\":\"Debt Category\",\"close_today_bal\":\"Closing Balance Today\"},\"dataTypes\":{\"record_date\":\"DATE\",\"debt_catg\":\"STRING\",\"close_today_bal\":\"CURRENCY0\"},\"dataFormats\":{\"record_date\":\"YYYY-MM-DD\",\"debt_catg\":\"String\",\"close_today_bal\":\"$1,000,000\"},\"total-count\":7,\"total-pages\":1},\"links\":{\"self\":\"&page%5Bnumber%5D=1&page%5Bsize%5D=100\",\"first\":\"&page%5Bnumber%5D=1&page%5Bsize%5D=100\",\"prev\":null,\"next\":null,\"last\":\"&page%5Bnumber%5D=1&page%5Bsize%5D=100\"}}";
    }
}