package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.domain.Account
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

        val accountToCreate = Account(null, account.accountName, account.dateCreate)
        val owners = account.owners.map { com.wizaord.moneyweb.domain.AccountOwner(it) }.toSet()
        val accountCreated = accountService.create(accountToCreate, owners)
        return ResponseEntity(Account(accountCreated.id, account.accountName, account.dateCreate), HttpStatus.CREATED)
    }

}

data class AccountCreate(var accountName: String,
                         var dateCreate: Date,
                         var owners: List<String>) {

}