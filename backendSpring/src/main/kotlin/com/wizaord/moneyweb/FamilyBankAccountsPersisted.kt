package com.wizaord.moneyweb

import com.wizaord.moneyweb.truedomain.InfrastructureBankAccountFamilyNotifications
import com.wizaord.moneyweb.truedomain.FamilyBankAccountsImpl
import org.slf4j.LoggerFactory

class FamilyBankAccountsPersisted(val familyName: String,
                                  val bankAccountPersistence: BankAccountPersistence): InfrastructureBankAccountFamilyNotifications {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val familyBankAccounts = FamilyBankAccountsImpl(this.familyName, this)

    init {
        logger.info("Init FamilyBankAccountsPersisted for family : {}", this.familyName)
        val familyBankAccounts = bankAccountPersistence.loadBankAccountByFamilyName(familyName)
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