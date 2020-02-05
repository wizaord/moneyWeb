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

    fun getServiceBeanForFamily(familyName: String): FamilyBankAccountsService {
        return applicationContext.getBean("familyBankAccountsService", familyName, familyBankAccountPersistence) as FamilyBankAccountsService
    }
}