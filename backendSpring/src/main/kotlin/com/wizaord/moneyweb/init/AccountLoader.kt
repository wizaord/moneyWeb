package com.wizaord.moneyweb.init

import com.wizaord.moneyweb.services.FamilyBankAccountServiceFactory
import com.wizaord.moneyweb.services.FamilyBankAccountsCreateService
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
        @Autowired val familyBankAccountServiceFactory: FamilyBankAccountServiceFactory,
        @Autowired val familyBankAccountsCreateService: FamilyBankAccountsCreateService
) {

    private val logger = LoggerFactory.getLogger(AccountLoader::class.java)
    private val accounts: MutableList<Account> = mutableListOf()
    val accountsMapping: MutableMap<String, String> = mutableMapOf()

    @Value("\${moneyweb.init.initdatabase.fileLocation.accounts}")
    lateinit var accountsFilePath: String

    fun readAccounts() {
        File(accountsFilePath).forEachLine {
            val splitStr = it.replace("\"", "").split(",")
            val isOpened = when (splitStr[5]) {
                "1" -> false
                else -> true
            }
            accounts.add(Account(splitStr[0], splitStr[1], splitStr[3], isOpened, createDateFromString(splitStr[4])))
            accountsMapping[splitStr[0]] = splitStr[1]
        }
    }

    fun loadAccounts() {
        readAccounts()

        familyBankAccountsCreateService.initFamily("mouilleron")

        val familyBean = this.familyBankAccountServiceFactory.getServiceBeanForFamily("mouilleron")

        accounts.forEach { account ->
            familyBean.accountRegister(account.name, "TO_BE_DEFINED", account.dateCreation.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
            if (!account.isOpen) familyBean.accountClose(account.name)

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
                   val isOpen: Boolean,
                   val dateCreation: Date)