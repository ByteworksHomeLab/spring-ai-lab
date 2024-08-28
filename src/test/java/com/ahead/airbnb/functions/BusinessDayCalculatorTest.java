package com.ahead.airbnb.functions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class BusinessDayCalculatorTest {

    @Test
    public void testFriday() {
        LocalDate today = LocalDate.of(2024, 8, 30);
        LocalDate previousBusinessDay = BusinessDayCalculator.getPreviousUSGovernmentBusinessDay(today);
        assert previousBusinessDay.equals(LocalDate.of(2024, 8, 29));;
    }
    @Test
    public void testMonday() {
        LocalDate today = LocalDate.of(2024, 8, 26);
        LocalDate previousBusinessDay = BusinessDayCalculator.getPreviousUSGovernmentBusinessDay(today);
        assert previousBusinessDay.equals(LocalDate.of(2024, 8, 23));;
    }

    @Test
    public void testDayAfterHoliday() {
        LocalDate today = LocalDate.of(2024, 7, 5);
        LocalDate previousBusinessDay = BusinessDayCalculator.getPreviousUSGovernmentBusinessDay(today);
        assert previousBusinessDay.equals(LocalDate.of(2024, 7, 3));;
    }

    @Test
    public void testHoliday() {
        LocalDate today = LocalDate.of(2024, 7, 4);
        LocalDate previousBusinessDay = BusinessDayCalculator.getPreviousUSGovernmentBusinessDay(today);
        assert previousBusinessDay.equals(LocalDate.of(2024, 7, 3));;
    }
}
