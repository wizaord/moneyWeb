package com.wizaord.moneyweb.init

import com.wizaord.moneyweb.infrastructure.CategoryFamilyPersistence
import com.wizaord.moneyweb.infrastructure.FamilyBankAccountPersistence
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


@Component
class InitRunner (
        @Autowired var categoryLoader: CategoryLoader,
        @Autowired var categoryFamilyPersistence: CategoryFamilyPersistence,
        @Autowired var accountLoader: AccountLoader,
        @Autowired var familyBankAccountPersistence: FamilyBankAccountPersistence,
        @Autowired var debitCreditLoader: DebitCreditLoader
        ): CommandLineRunner {
    private val logger = LoggerFactory.getLogger(InitRunner::class.java)

    @Value("\${moneyweb.init.initdatabase}")
    lateinit var activateDatabaseInitialisation: String

    override fun run(vararg args: String?) {
        if (activateDatabaseInitialisation == "false") {
            logger.info("Database initialisation is deactivated")
            return
        }
        cleanDatabase()
        // load account
        accountLoader.loadAccounts()

        categoryLoader.loadCategories()

        val accountsMap = accountLoader.accountsMapping

        // load transactions
        debitCreditLoader.loadFamilyBankAccount("mouilleron")
        debitCreditLoader.loadDebitCredit(accountsMap)
    }

    private fun cleanDatabase() {
        familyBankAccountPersistence.transactionDeleteAll()
        familyBankAccountPersistence.familyBankAccountDeleteAll()
        categoryFamilyPersistence.deleteAll()
    }
}