package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.configuration.toDate
import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.services.FamilyBankAccountServiceFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/moneyapi/family/{familyName}/accounts/{accountName}/transactions")
class TransactionController(
        @Autowired private val familyBankAccountServiceFactory: FamilyBankAccountServiceFactory
) {

    @GetMapping("")
    @ResponseBody
    fun getTransactions(@PathVariable familyName: String,
                        @PathVariable accountName: String): List<com.wizaord.moneyweb.controllers.Transaction> {
        val familyService = familyBankAccountServiceFactory.getFamilyServiceWithoutTransactions(familyName)
        return familyService.transactions(accountName).map { com.wizaord.moneyweb.controllers.Transaction.fromDomain(it) }
    }
}


data class Transaction(val id: String,
                       val amount: Double,
                       val userLibelle: String,
                       val bankLibelle: String,
                       val bankDetail: String?,
                       val isPointe: Boolean,
                       val dateCreation: Date
                       ) {

    companion object {
        fun fromDomain(t: Transaction): com.wizaord.moneyweb.controllers.Transaction {
            return com.wizaord.moneyweb.controllers.Transaction(t.id, t.amount, t.userLibelle, t.bankLibelle, t.bankDetail, t.isPointe, t.dateCreation.toDate())
        }
    }
}