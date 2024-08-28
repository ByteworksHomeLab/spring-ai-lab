package com.ahead.airbnb.functions;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.Set;

public class BusinessDayCalculator {

    public static Set<LocalDate> getUSFederalHolidays(int year) {
        Set<LocalDate> holidays = new HashSet<>();

        // New Year's Day
        holidays.add(LocalDate.of(year, Month.JANUARY, 1));

        // Martin Luther King Jr. Day (third Monday of January)
        holidays.add(LocalDate.of(year, Month.JANUARY, 1).with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY)));

        // Presidents' Day (third Monday of February)
        holidays.add(LocalDate.of(year, Month.FEBRUARY, 1).with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY)));

        // Memorial Day (last Monday of May)
        holidays.add(LocalDate.of(year, Month.MAY, 1).with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY)));

        // Juneteenth National Independence Day
        holidays.add(LocalDate.of(year, Month.JUNE, 19));

        // Independence Day
        holidays.add(LocalDate.of(year, Month.JULY, 4));

        // Labor Day (first Monday of September)
        holidays.add(LocalDate.of(year, Month.SEPTEMBER, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)));

        // Columbus Day (second Monday of October)
        holidays.add(LocalDate.of(year, Month.OCTOBER, 1).with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.MONDAY)));

        // Veterans Day
        holidays.add(LocalDate.of(year, Month.NOVEMBER, 11));

        // Thanksgiving Day (fourth Thursday of November)
        holidays.add(LocalDate.of(year, Month.NOVEMBER, 1).with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY)));

        // Christmas Day
        holidays.add(LocalDate.of(year, Month.DECEMBER, 25));

        return holidays;
    }

    public static LocalDate getPreviousUSGovernmentBusinessDay(LocalDate date) {
        LocalDate previousBusinessDay = date.minusDays(1);

        while (isWeekend(previousBusinessDay) || isFederalHoliday(previousBusinessDay)) {
            previousBusinessDay = previousBusinessDay.minusDays(1);
        }

        return previousBusinessDay;
    }

    private static boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private static boolean isFederalHoliday(LocalDate date) {
        return getUSFederalHolidays(date.getYear()).contains(date);
    }
}
