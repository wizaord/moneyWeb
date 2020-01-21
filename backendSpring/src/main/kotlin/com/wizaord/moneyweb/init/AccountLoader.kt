package com.wizaord.moneyweb.init

import com.wizaord.moneyweb.domain.Account
import com.wizaord.moneyweb.domain.AccountOwner
import com.wizaord.moneyweb.services.AccountService
import com.wizaord.moneyweb.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.*

@Component
class AccountLoader(
        @Autowired val userService: UserService,
        @Autowired val accountService: AccountService
) {

    private val logger = LoggerFactory.getLogger(AccountLoader::class.java)
    private val accounts: MutableList<Account> = mutableListOf()

    @Value("\${moneyweb.init.initdatabase.fileLocation.accounts}")
    lateinit var accountsFilePath: String

    fun readAccounts() {
        File(accountsFilePath).forEachLine {
            val splitStr = it.replace("\"", "").split(",")
            accounts.add(Account(splitStr[0], splitStr[1], "TO_BE_DEFINED", createDateFromString(splitStr[4])))
        }
    }

    fun loadAccounts() {
        readAccounts()

        val me = userService.getUserByUsernameAndPassword("mouilleron", "Wizard38")
        accounts.forEach {account ->
            account.owners.add(AccountOwner(me!!.owners.elementAt(0).name))
            accountService.create(account, me)
        }
        logger.info("All accounts have been loaded")
    }

    fun createDateFromString(dateSrt: String): Date {
        return Date.from(LocalDate.parse(dateSrt).atStartOfDay(ZoneId.systemDefault()).toInstant())
    }
}