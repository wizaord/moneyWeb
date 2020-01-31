package com.wizaord.moneyweb.truedomain

import com.wizaord.moneyweb.truedomain.exceptions.BankAccountWithTheSameNameException
import com.wizaord.moneyweb.truedomain.exceptions.FamilyMemberAlreadyExistException
import com.wizaord.moneyweb.truedomain.exceptions.FamilyMemberNotKnowException
import com.wizaord.moneyweb.truedomain.exceptions.FamilyMemberOwnerException
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
    override fun registerAccount(bankAccountImpl: BankAccountImpl, owners: List<FamilyMember>) {
        accessToAccountByAccountName(bankAccountImpl.name)?.apply { throw BankAccountWithTheSameNameException() }
        if (!isMemberAreRegistered(owners)) {
            throw FamilyMemberNotKnowException()
        }

        this.bankAccountsOwners.add(BankAccountOwners(bankAccountImpl, owners.toMutableList()))
        this.notifyBankAccountUpdated()
    }

    @Throws(BankAccountWithTheSameNameException::class)
    override fun registerAccount(bankAccountImpl: BankAccountImpl) {
        registerAccount(bankAccountImpl, familyMembers)
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
    override fun accessToAccountsByBankname(bankName: String) = accessToAccounts().filter { it.bankAccountImpl.bankName == bankName }
    override fun accessToAccountsByFamilyMember(familyMember: FamilyMember) = accessToAccounts().filter { it.hasOwner(familyMember) }
    override fun accessToAccountByAccountName(accountName: String) = accessToAccounts().firstOrNull { it.bankAccountImpl.name == accountName }

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

    private fun notifyBankAccountUpdated() {
        logger.info("FamilyAccount {} updated", familyName)
        this.infrastructureBankAccountFamilyNotifications.notifyFamilyBankAccountUpdate(this)
    }
}


