package com.wizaord.moneyweb.domain

import com.wizaord.moneyweb.domain.exceptions.BankAccountWithTheSameNameException
import com.wizaord.moneyweb.domain.exceptions.FamilyMemberAlreadyExistException
import com.wizaord.moneyweb.domain.exceptions.FamilyMemberNotKnowException
import com.wizaord.moneyweb.domain.exceptions.FamilyMemberOwnerException
import com.wizaord.moneyweb.domain.transactions.Transaction
import org.slf4j.LoggerFactory

data class FamilyBankAccountsImpl(
        val familyName: String,
        private val infrastructureBankAccountFamilyNotifications: InfrastructureBankAccountFamilyNotifications? = null
): FamilyBankAccounts {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    val bankAccountsOwners = mutableListOf<BankAccountOwners>()
    private val familyMembers = mutableListOf<FamilyMember>()
    private var notificationActivated = true


    @Throws(BankAccountWithTheSameNameException::class,
            FamilyMemberNotKnowException::class)
    override fun registerAccount(bankAccount: BankAccount, owners: List<FamilyMember>) {
        accessToAccountByAccountName(bankAccount.getName())?.apply { throw BankAccountWithTheSameNameException() }
        if (!isMemberAreRegistered(owners)) {
            throw FamilyMemberNotKnowException()
        }

        this.bankAccountsOwners.add(BankAccountOwners(bankAccount, owners.toMutableList()))
        this.notifyBankAccountUpdated()
    }

    @Throws(BankAccountWithTheSameNameException::class)
    override fun registerAccount(bankAccount: BankAccount) {
        registerAccount(bankAccount, familyMembers)
    }

    override fun changeBankAccountOwners(bankAccountName: String, owners: List<FamilyMember>) {
        val accessToAccountByAccountName = accessToAccountByAccountName(bankAccountName) ?: throw NoSuchElementException()
        accessToAccountByAccountName.replaceOwners(owners)
        infrastructureBankAccountFamilyNotifications?.notifyFamilyBankAccountUpdate(this)
    }


    private fun isMemberAreRegistered(familyMembersToCheck: List<FamilyMember>): Boolean {
        val nbUserNotRegistered = familyMembersToCheck
                .filter { !this.familyMembers.contains(it) }
                .count()
        return nbUserNotRegistered == 0
    }

    override fun accessToAccounts() = this.bankAccountsOwners.toList()
    override fun accessToAccountsByBankname(bankName: String) = accessToAccounts().filter { it.bankAccount.getBankName() == bankName }
    override fun accessToAccountsByFamilyMember(familyMember: FamilyMember) = accessToAccounts().filter { it.hasOwner(familyMember) }
    override fun accessToAccountByAccountName(accountName: String) = accessToAccounts().firstOrNull { it.bankAccount.getName() == accountName }

    @Throws(FamilyMemberAlreadyExistException::class)
    override fun registerFamilyMember(familyMember: FamilyMember) {
        if (isMemberAreRegistered(listOf(familyMember))) {
            throw FamilyMemberAlreadyExistException()
        }
        familyMembers.add(familyMember)
        this.notifyBankAccountUpdated()
    }

    @Throws(FamilyMemberOwnerException::class)
    override fun removeFamilyMember(familyMember: FamilyMember) {
        if (accessToAccountsByFamilyMember(familyMember).isNotEmpty()) {
            throw FamilyMemberOwnerException()
        }
        familyMembers.remove(familyMember)
        this.notifyBankAccountUpdated()
    }

    override fun getFamily() = this.familyMembers.toList()

    override fun getCategoryConfiguredByPreviousFamilyTransaction(transaction: Transaction): Transaction? {
        return this.bankAccountsOwners
                .asSequence()
                .map { it.bankAccount.getTransactionsMatched(transaction) }
                .flatten()
                .sortedBy { it.matchPoint }
                .map { it.transaction }
                .first()
    }

    private fun notifyBankAccountUpdated() {
        if (notificationActivated) {
            this.infrastructureBankAccountFamilyNotifications?.notifyFamilyBankAccountUpdate(this)
        }

    }

    fun deactivateNotifications() {
        notificationActivated = false
    }

    fun activateNotifications() {
        notificationActivated = true
    }
}


