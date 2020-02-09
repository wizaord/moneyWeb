package com.wizaord.moneyweb.init

import com.wizaord.moneyweb.infrastructure.domain.CategoryFamilyRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


@Component
class InitRunner (
        @Autowired var categoryLoader: CategoryLoader,
        @Autowired var categoryFamilyRepository: CategoryFamilyRepository,
        @Autowired var accountLoader: AccountLoader
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

//        // load transactions
//        debitCreditLoader.loadDebitCredit()
//        debitCreditLoader.loadDetailMontant()
//        debitCreditLoader.loadEveythingInMongo()
//        // load ventilations
    }

    private fun cleanDatabase() {
        categoryFamilyRepository.deleteAll()
    }
}