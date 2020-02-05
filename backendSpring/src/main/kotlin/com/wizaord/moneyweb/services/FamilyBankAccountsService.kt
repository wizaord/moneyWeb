package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import com.wizaord.moneyweb.domain.InfrastructureBankAccountFamilyNotifications
import com.wizaord.moneyweb.domain.InfrastructureBankAccountNotifications
import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.infrastructure.FamilyBankAccountPersistence
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("prototype")
class FamilyBankAccountsService(val familyName: String,
                                @Autowired val familyBankAccountPersistence: FamilyBankAccountPersistence): InfrastructureBankAccountFamilyNotifications, InfrastructureBankAccountNotifications {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    val familyBankAccounts = FamilyBankAccountsImpl(this.familyName, this)

    init {
        logger.info("FamilyBankAccountService will be initialised with family : {}", this.familyName)
        val familyBankAccounts = familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(familyName)
        if (familyBankAccounts != null) {
            updateFamilyBankAccount(familyBankAccounts)
        }
        logger.info("FamilyBankAccountService successfully initialised")

    }

    private fun updateFamilyBankAccount(familyBankAccountsImpl: FamilyBankAccountsImpl) {
        familyBankAccounts.deactivateNotifications()
        familyBankAccountsImpl.getFamily().forEach {
            familyBankAccounts.registerFamilyMember(it)
        }
        familyBankAccounts.bankAccountsOwners.addAll(familyBankAccountsImpl.bankAccountsOwners)
        familyBankAccounts.activateNotifications()
    }


    fun getOwners() = familyBankAccounts.getFamily()


    override fun notifyFamilyBankAccountUpdate(familyBankAccountsImpl: FamilyBankAccountsImpl) {
        logger.info("FamilyAccount has been updated")
    }

    override fun notifyNewTransaction(transaction: Transaction) {
        logger.info("new Transaction has been created")
    }

    override fun notifyRemoveTransaction(transaction: Transaction) {
        logger.info("Transaction has been deleted")
    }

}