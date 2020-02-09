package com.wizaord.moneyweb.init

import com.wizaord.moneyweb.services.FamilyBankAccountServiceFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Component
class AccountLoader(
        @Autowired val familyBankAccountServiceFactory: FamilyBankAccountServiceFactory
) {

    private val logger = LoggerFactory.getLogger(AccountLoader::class.java)
    private val accounts: MutableList<Account> = mutableListOf()

    @Value("\${moneyweb.init.initdatabase.fileLocation.accounts}")
    lateinit var accountsFilePath: String

    fun readAccounts() {
        File(accountsFilePath).forEachLine {
            val splitStr = it.replace("\"", "").split(",")
            accounts.add(Account(splitStr[0], splitStr[1], splitStr[3], createDateFromString(splitStr[4])))
        }
    }

    fun loadAccounts() {
        readAccounts()

        val familyBean = this.familyBankAccountServiceFactory.getServiceBeanForFamily("mouilleron")

        accounts.forEach { account ->
            familyBean.accountRegister(account.name, "TO_BE_DEFINED", account.dateCreation.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
        }
        logger.info("All accounts have been loaded")
    }

    fun createDateFromString(dateSrt: String): Date {
        return Date.from(LocalDate.parse(dateSrt).atStartOfDay(ZoneId.systemDefault()).toInstant())
    }
}

// "31","BO - compte courant",,"2242.24","2013-07-22","0","0",NULL,"7"
data class Account(val id: String,
                   val name: String,
                   val amount: String,
                   val dateCreation: Date)