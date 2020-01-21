package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.services.AccountService
import com.wizaord.moneyweb.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_ACCEPTABLE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/moneyapi/accounts")
class AccountController(
        @Autowired var userService: UserService,
        @Autowired var accountService: AccountService
) {

    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)

    @PostMapping("/create")
    @ResponseBody
    fun create(@RequestBody account: AccountCreate): ResponseEntity<Account> {
        val nbNotValidOwner = account.owners
                .map { userService.isKnowOwner(it) }
                .filter { !it }
                .count()

        if (nbNotValidOwner > 0) {
            return ResponseEntity(NOT_ACCEPTABLE);
        }

        val owners = account.owners.map { com.wizaord.moneyweb.domain.AccountOwner(it) }.toMutableSet()
        val accountToCreate = com.wizaord.moneyweb.domain.Account(null, account.accountName, account.bankName, account.dateCreate, true, owners)

        val accountCreated = accountService.create(accountToCreate)

        return ResponseEntity(Account.fromAccountDb(accountCreated), HttpStatus.CREATED)
    }

    @RequestMapping("")
    @ResponseBody
    fun accounts(): List<Account> {
        logger.info("Demande de la liste de comptes")
        return this.accountService.getAllUserAccounts()
                .map(Account.Companion::fromAccountDb);
    }

}

data class AccountCreate(var accountName: String,
                         var bankName: String,
                         var dateCreate: Date,
                         var owners: List<String>) {

}

class Account(var id: String,
              var accountName: String,
              var bankName: String,
              var dateCreate: Date,
              var isOpened: Boolean,
              var owners: List<String>) {

    companion object {
        fun fromAccountDb(account: com.wizaord.moneyweb.domain.Account): Account {
            return Account(account.id!!,
                    account.name,
                    account.bankName,
                    account.openDate,
                    account.isOpened,
                    account.owners.map { it.name }.toList())
        }
    }
}



