package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.InfrastructureBankAccountFamilyNotifications
import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import org.slf4j.LoggerFactory

class FamilyBankAccountsService(val familyName: String,
                                val familyBankAccountPersistence: FamilyBankAccountPersistence): InfrastructureBankAccountFamilyNotifications {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val familyBankAccounts = FamilyBankAccountsImpl(this.familyName, this)

    init {
        logger.info("Init FamilyBankAccountsPersisted for family : {}", this.familyName)
        val familyBankAccounts = familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(familyName)
        if (familyBankAccounts != null) {
            updateFamilyBankAccount(familyBankAccounts)
        }
    }

    private fun updateFamilyBankAccount(familyBankAccountsImpl: FamilyBankAccountsImpl) {
//        familyBankAccountsImpl.getFamily().forEach {
//            familyBankAccounts.registerFamilyMember(it)
//        }
//        familyBankAccounts.bankAccountsOwners.addAll(familyBankAccountsImpl.bankAccountsOwners)
    }

    override fun notifyFamilyBankAccountUpdate(familyBankAccountsImpl: FamilyBankAccountsImpl) {
        logger.info("FamilyAccount has been updated")
    }

}