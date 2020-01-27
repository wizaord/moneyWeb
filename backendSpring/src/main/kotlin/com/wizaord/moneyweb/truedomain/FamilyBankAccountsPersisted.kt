package com.wizaord.moneyweb.truedomain

import org.slf4j.LoggerFactory

class FamilyBankAccountsPersisted(familyName: String,
                                  bankAccountPersistence: BankAccountPersistence) : FamilyBankAccounts(familyName) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    init {
        logger.info("Init FamilyBankAccountsPersisted for family : {}", this.familyName)
        val familyBankAccounts = bankAccountPersistence.loadBankAccountByFamilyName(familyName)
        if (familyBankAccounts != null) {
            updateFamilyBankAccount(familyBankAccounts)
        }
    }

    private fun updateFamilyBankAccount(familyBankAccounts: FamilyBankAccounts) {
        this.familyMembers.addAll(familyBankAccounts.familyMembers)
        this.bankAccountsOwners.addAll(familyBankAccounts.bankAccountsOwners)
    }

}