package com.wizaord.moneyweb

import com.wizaord.moneyweb.domain.AccountRepository
import com.wizaord.moneyweb.domain.CategoryFamily
import com.wizaord.moneyweb.domain.CategoryFamilyRepository
import com.wizaord.moneyweb.domain.SubCategory
import com.wizaord.moneyweb.init.AccountLoader
import com.wizaord.moneyweb.init.CategoryLoader
import com.wizaord.moneyweb.init.DebitCreditLoader
import com.wizaord.moneyweb.services.CategoryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.File
import java.util.*


@Component
@Profile("init-database")
class InitRunner (
        @Autowired var categoryLoader: CategoryLoader,
        @Autowired var categoryFamilyRepository: CategoryFamilyRepository,
        @Autowired var accountRepository: AccountRepository,
        @Autowired var accountLoader: AccountLoader,
        @Autowired var debitCreditLoader: DebitCreditLoader
): CommandLineRunner {
    private val logger = LoggerFactory.getLogger(InitRunner::class.java)

    override fun run(vararg args: String?) {
        categoryFamilyRepository.deleteAll()
        accountRepository.deleteAll()

        // load categories
        val categoryRecords = categoryLoader.readCategories()
        categoryLoader.loadCategories(categoryRecords)

        // load account
        val accounts = accountLoader.readAccounts()
        accountLoader.loadAccounts(accounts)

        // load transactions
        val debitCredits = debitCreditLoader.readDebitCredits();
//        val ventilations = debitCreditLoader.readVentilations();

        // load ventilations
    }
}