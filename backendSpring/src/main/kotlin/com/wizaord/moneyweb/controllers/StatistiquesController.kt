package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.configuration.toMonthString
import com.wizaord.moneyweb.domain.statistiques.AccountMonthStatistiques
import com.wizaord.moneyweb.domain.statistiques.AccountStatistiques
import com.wizaord.moneyweb.services.StatistiquesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/moneyapi/family/{familyName}/statistiques")
class StatistiquesController(
        @Autowired private val statistiquesService: StatistiquesService
) {

    @GetMapping("/accounts")
    @ResponseBody
    fun accountsSoldeStatisques(@PathVariable familyName: String): List<AccountStatistiquesC> {
        return statistiquesService.getAccountSoldeByMonth(familyName)
                .map { AccountStatistiquesC.fromDomain(it) }
    }
}

data class AccountStatistiquesC(
        val accountName: String,
        val monthStatistiques: List<AccountMonthStatistiquesC>,
        val owners: List<String>
)
{
    companion object {
        fun fromDomain(accS: AccountStatistiques): AccountStatistiquesC {
            return AccountStatistiquesC(accS.accountName,
                    accS.monthStatistiques.map { AccountMonthStatistiquesC.fromDomain(it) },
                    accS.owners)

        }
    }
}

data class AccountMonthStatistiquesC(
        val month: String,
        var revenus: Double,
        var depenses: Double
) {
    companion object {
        fun fromDomain(accS: AccountMonthStatistiques): AccountMonthStatistiquesC {
            return AccountMonthStatistiquesC(accS.month.toMonthString(), accS.revenus, accS.depenses)

        }
    }
}