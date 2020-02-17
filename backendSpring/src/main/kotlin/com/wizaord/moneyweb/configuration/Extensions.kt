package com.wizaord.moneyweb.configuration

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun LocalDate.toDate(): Date {
    return Date.from(this.atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant());
}


fun Date.toLocalDate(): LocalDate {
    return this.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
}