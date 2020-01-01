package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.domain.Account
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/moneyapi/accounts")
class AccountController {

    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)

    @PostMapping("/create")
    @ResponseBody
    fun create(@RequestBody account: AccountCreate): Account {
        return Account(null, account.accountName, account.dateCreate)
    }

}

data class AccountCreate(var accountName: String, var dateCreate: Date) {

}