package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.configuration.toLocalDateMonth
import com.wizaord.moneyweb.domain.statistiques.AccountMonthStatistiques
import com.wizaord.moneyweb.domain.statistiques.AccountStatistiques
import org.springframework.stereotype.Service
import kotlin.math.absoluteValue

@Service
class StatistiquesService(
        private val familyBankAccountServiceFactory: FamilyBankAccountServiceFactory
) {

    fun getAccountSoldeByMonth(familyName: String): List<AccountStatistiques> {
        val result = mutableListOf<AccountStatistiques>()
        val familyServiceWithTransactions = familyBankAccountServiceFactory.getFamilyServiceWithTransactions(familyName)
        familyServiceWithTransactions.bankAccountsName().forEach { accountName ->
            val accountOwners = familyServiceWithTransactions.bankAccountOwners(accountName)
            val transactionGroupByMonth = familyServiceWithTransactions.transactionsNotInternal(accountName)
                    .asSequence()
                    .map {
                        val revenus = if (it.amount >= 0) it.amount else 0.0
                        val depenses = if (it.amount >= 0) 0.0 else it.amount.absoluteValue
                        AccountMonthStatistiques(it.dateCreation.toLocalDateMonth(), revenus, depenses)
                    }
                    .groupingBy { it.month }
                    .reduce { key, acc, elt ->
                        acc.depenses += elt.depenses
                        acc.revenus += elt.revenus
                        acc
                    }
                    .map { it.value }
                    .sortedBy { it.month }
                    .toList()

            result.add(AccountStatistiques(accountName, transactionGroupByMonth, accountOwners))
        }
        return result
    }
}