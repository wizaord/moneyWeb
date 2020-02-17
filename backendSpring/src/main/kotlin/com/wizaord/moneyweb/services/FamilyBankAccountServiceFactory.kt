package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.infrastructure.FamilyBankAccountPersistence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service

@Service
class FamilyBankAccountServiceFactory(
        @Autowired val applicationContext: ApplicationContext,
        @Autowired val familyBankAccountPersistence: FamilyBankAccountPersistence
) {

    fun getFamilyServiceWithoutTransactions(familyName: String): FamilyBankAccountsService {
        val loadTransactions = false
        return applicationContext.getBean("familyBankAccountsService", familyName, loadTransactions, familyBankAccountPersistence) as FamilyBankAccountsService
    }

    fun getFamilyServiceWithTransactions(familyName: String): FamilyBankAccountsService {
        val loadTransactions = true
        return applicationContext.getBean("familyBankAccountsService", familyName, loadTransactions, familyBankAccountPersistence) as FamilyBankAccountsService
    }
}