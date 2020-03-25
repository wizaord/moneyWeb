package com.wizaord.moneyweb.domain.statistiques

import java.time.LocalDate

data class AccountStatistiques(
        val accountName: String,
        val monthStatistiques : List<AccountMonthStatistiques>,
        val owners: List<String>
)

data class AccountMonthStatistiques(
        val month: LocalDate,
        var revenus: Double,
        var depenses: Double
)
