package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.configuration.toDate
import com.wizaord.moneyweb.configuration.toLocalDate
import com.wizaord.moneyweb.domain.BankAccountImpl
import com.wizaord.moneyweb.domain.BankAccountOwners
import com.wizaord.moneyweb.services.FamilyBankAccountServiceFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/moneyapi/family/{familyName}/accounts")
class AccountController(
        @Autowired private val familyBankAccountServiceFactory: FamilyBankAccountServiceFactory
) {

    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)

    @PostMapping("")
    @ResponseBody
    fun create(@PathVariable familyName: String, @RequestBody account: AccountCreate): ResponseEntity<Account> {
        val familyService = familyBankAccountServiceFactory.getServiceBeanForFamily(familyName)
        familyService.accountRegister(account.accountName, account.bankName, account.dateCreate.toLocalDate(), account.owners)
        return ResponseEntity.ok().build()
    }

    @RequestMapping("")
    @ResponseBody
    fun accounts(@PathVariable familyName: String): List<Account> {
        val familyService = familyBankAccountServiceFactory.getServiceBeanForFamily(familyName)
        return familyService.bankAccounts().map { Account.fromDomain(it) }

    }

}


data class AccountCreate(var accountName: String,
                         var bankName: String,
                         var dateCreate: Date,
                         var owners: List<String>) {

}

class Account(var accountName: String,
              var bankName: String,
              var dateCreate: Date,
              var isOpened: Boolean,
              var owners: List<String>) {

    companion object {
        fun fromDomain(bao: BankAccountOwners): Account {
            val bankAccount = bao.bankAccount as BankAccountImpl
            return Account(bankAccount.getName(), bankAccount.getBankName(), bankAccount.dateCreation.toDate(),
                    bankAccount.isOpen, bao.getOwners().map { it.username })
        }
    }
}





