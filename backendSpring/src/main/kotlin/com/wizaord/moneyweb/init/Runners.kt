package com.wizaord.moneyweb.init

import com.wizaord.moneyweb.infrastructure.domain.CategoryFamilyRepository
import com.wizaord.moneyweb.infrastructure.domain.FamilyBankAccountsRepository
import com.wizaord.moneyweb.infrastructure.domain.TransactionsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


@Component
class InitRunner (
        @Autowired var categoryLoader: CategoryLoader,
        @Autowired var categoryFamilyRepository: CategoryFamilyRepository,
        @Autowired var accountLoader: AccountLoader,
        @Autowired var familyBankAccountsRepository: FamilyBankAccountsRepository,
        @Autowired var debitCreditLoader: DebitCreditLoader,
        @Autowired var transactionsRepository: TransactionsRepository
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
        categoryLoader.loadCategories()

        // load account
        accountLoader.loadAccounts()
        val accountsMap = accountLoader.accountsMapping

        // load transactions
        debitCreditLoader.loadFamilyBankAccount("mouilleron")
        debitCreditLoader.loadDebitCredit(accountsMap)
//        debitCreditLoader.loadDetailMontant()
//        debitCreditLoader.loadEveythingInMongo()
        // load ventilations
    }

    private fun cleanDatabase() {
        categoryFamilyRepository.deleteAll()
        familyBankAccountsRepository.deleteAll()
        transactionsRepository.deleteAll()

    }
}