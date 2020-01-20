package com.wizaord.moneyweb.init

import com.wizaord.moneyweb.domain.Account
import com.wizaord.moneyweb.domain.AccountOwner
import com.wizaord.moneyweb.services.AccountService
import com.wizaord.moneyweb.services.UserService
import org.springframework.beans.factory.annotation.Autowired
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

    fun readAccounts(): List<Account> {
        val records: MutableList<Account> = mutableListOf()
        File(javaClass.classLoader.getResource("init" + File.separator + "comptebancaire.csv").file).forEachLine {
            val splitStr = it.replace("\"", "").split(",")
            records.add(Account(splitStr[0], splitStr[1], "TO_BE_DEFINED", createDateFromString(splitStr[4])))
        }
        return records
    }

    fun loadAccounts(accounts: List<Account>) {
        val me = userService.getUserByUsernameAndPassword("mouilleron", "Wizard38")
        accounts.forEach {account ->
            account.owners.add(AccountOwner(me!!.owners.elementAt(0).name))
            accountService.create(account, me)
        }
    }

    fun createDateFromString(dateSrt: String): Date {
        return Date.from(LocalDate.parse(dateSrt).atStartOfDay(ZoneId.systemDefault()).toInstant())
    }
}