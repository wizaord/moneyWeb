package com.wizaord.moneyweb.truedomain

import com.wizaord.moneyweb.truedomain.exceptions.BankAccountWithTheSameNameException
import com.wizaord.moneyweb.truedomain.exceptions.FamilyMemberAlreadyExistException
import com.wizaord.moneyweb.truedomain.exceptions.FamilyMemberNotKnowException
import com.wizaord.moneyweb.truedomain.exceptions.FamilyMemberOwnerException
import com.wizaord.moneyweb.truedomain.transactions.Transaction
import org.slf4j.LoggerFactory

open class FamilyBankAccountsImpl(
        val familyName: String,
        private val infrastructureBankAccountFamilyNotifications: InfrastructureBankAccountFamilyNotifications
): FamilyBankAccounts {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    val bankAccountsOwners = mutableListOf<BankAccountOwners>()
    private val familyMembers = mutableListOf<FamilyMember>()


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
        infrastructureBankAccountFamilyNotifications.notifyFamilyBankAccountUpdate(this)
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
        logger.info("FamilyAccount {} updated", familyName)
        this.infrastructureBankAccountFamilyNotifications.notifyFamilyBankAccountUpdate(this)
    }
}


