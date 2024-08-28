package com.ahead.airbnb.records;

/**
 * Record representing a debt entry.
 *
 * @param record_date the date of the record
 * @param debt_catg the category of the debt
 * @param close_today_bal the closing balance of the debt for the day
 */
public record DebtRecord(String record_date, String debt_catg, String close_today_bal) {
}
