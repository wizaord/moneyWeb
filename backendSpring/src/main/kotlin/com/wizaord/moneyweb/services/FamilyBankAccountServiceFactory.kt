package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.infrastructure.FamilyBankAccountPersistence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service

@Service
class FamilyBankAccountServiceFactory(
        @Autowired val applicationContext: ApplicationContext,
        @Autowired val familyBankAccountPersistence: FamilyBankAccountPersistence,
        @Autowired val categoryService: CategoryService
) {

    fun getFamilyServiceWithoutTransactions(familyName: String): FamilyBankAccountsService {
        val familyBankAccountsService = applicationContext.getBean("familyBankAccountsService", familyBankAccountPersistence, categoryService) as FamilyBankAccountsService
        familyBankAccountsService.loadFamilyBankFromPersistence(familyName)
        return familyBankAccountsService
    }

    fun getFamilyServiceWithTransactions(familyName: String): FamilyBankAccountsService {
        val familyServiceBean = getFamilyServiceWithoutTransactions(familyName)
        familyServiceBean.loadTransactionsFromPersistence();
        return familyServiceBean
    }

    fun getFamilyServiceForAccountWithTransactions(familyName: String, accountName: String): FamilyBankAccountsService {
        val familyServiceBean = getFamilyServiceWithoutTransactions(familyName)
        familyServiceBean.keepOnlyBankAccount(accountName)
        familyServiceBean.loadTransactionsFromPersistence();
        return familyServiceBean
    }

}