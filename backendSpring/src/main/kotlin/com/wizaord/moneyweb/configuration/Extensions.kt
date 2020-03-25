package com.wizaord.moneyweb.configuration

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun LocalDate.toDate(): Date {
    return Date.from(this.atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant());
}

fun LocalDate.toLocalDateMonth(): LocalDate {
    return LocalDate.of(this.year, this.month, 1)
}

fun LocalDate.toMonthString(): String {
    return "${this.year}-${this.monthValue}"
}


fun Date.toLocalDate(): LocalDate {
    return this.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
}