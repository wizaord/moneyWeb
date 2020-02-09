package com.wizaord.moneyweb.configuration

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun LocalDate.toDate(): Date {
    return java.util.Date.from(this.atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant());
}