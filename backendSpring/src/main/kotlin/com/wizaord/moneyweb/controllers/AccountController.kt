package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.configuration.toDate
import com.wizaord.moneyweb.domain.BankAccountImpl
import com.wizaord.moneyweb.domain.BankAccountOwners
import com.wizaord.moneyweb.services.FamilyBankAccountServiceFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/moneyapi/family/{familyName}/accounts")
class AccountController(
        @Autowired private val familyBankAccountServiceFactory: FamilyBankAccountServiceFactory
) {

    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)

//    @PostMapping("/create")
//    @ResponseBody
//    fun create(@RequestBody account: AccountCreate): ResponseEntity<Account> {
//        val nbNotValidOwner = account.owners
//                .map { userService.isKnowOwner(it) }
//                .filter { !it }
//                .count()
//
//        if (nbNotValidOwner > 0) {
//            return ResponseEntity(NOT_ACCEPTABLE);
//        }
//
//        val owners = account.owners.map { com.wizaord.moneyweb.infrastructure.AccountOwner(it) }.toMutableSet()
//        val accountToCreate = com.wizaord.moneyweb.infrastructure.Account(null, account.accountName, account.bankName, account.dateCreate, true, owners)
//
//        val accountCreated = accountService.create(accountToCreate)
//
//        return ResponseEntity(Account.fromAccountDb(accountCreated), HttpStatus.CREATED)
//    }

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





