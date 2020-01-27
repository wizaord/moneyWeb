package com.wizaord.moneyweb.truedomain

import org.slf4j.LoggerFactory

open class FamilyBankAccounts(
        val familyName: String
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    val bankAccountsOwners = mutableListOf<BankAccountOwners>()
    val familyMembers = mutableListOf<FamilyMember>()


    @Throws(BankAccountWithTheSameNameException::class,
            FamilyMemberNotKnowException::class)
    fun registerAccount(bankAccount: BankAccount, owners: List<FamilyMember>) {
        accessToAccountByAccountName(bankAccount.name)?.apply { throw BankAccountWithTheSameNameException() }
        if (!isMemberAreRegistered(owners)) {
            throw FamilyMemberNotKnowException()
        }

        this.bankAccountsOwners.add(BankAccountOwners(bankAccount, owners.toMutableList()))
    }

    @Throws(BankAccountWithTheSameNameException::class)
    fun registerAccount(bankAccount: BankAccount) = registerAccount(bankAccount, familyMembers)

    private fun isMemberAreRegistered(familyMembersToCheck: List<FamilyMember>): Boolean {
        val nbUserNotRegistered = familyMembersToCheck
                .filter { !this.familyMembers.contains(it) }
                .count()
        return nbUserNotRegistered == 0
    }

    fun accessToAccounts() = this.bankAccountsOwners.toList()
    fun accessToAccountsByBankname(bankName: String) = accessToAccounts().filter { it.bankAccount.bankName == bankName }
    fun accessToAccountsByFamilyMember(familyMember: FamilyMember) = accessToAccounts().filter { it.hasOwner(familyMember) }
    fun accessToAccountByAccountName(accountName: String) = accessToAccounts().firstOrNull { it.bankAccount.name == accountName }

    @Throws(FamilyMemberAlreadyExistException::class)
    fun registerFamilyMember(familyMember: FamilyMember) {
        if (isMemberAreRegistered(listOf(familyMember))) {
            throw FamilyMemberAlreadyExistException()
        }
        familyMembers.add(familyMember)
    }

    @Throws(FamilyMemberOwnerException::class)
    fun removeFamilyMember(familyMember: FamilyMember) {
        if (accessToAccountsByFamilyMember(familyMember).isNotEmpty()) {
            throw FamilyMemberOwnerException()
        }
        familyMembers.remove(familyMember)
    }
}

data class FamilyMember(val username: String)

class BankAccountWithTheSameNameException : Exception()
class FamilyMemberNotKnowException : Exception()
class FamilyMemberAlreadyExistException : Exception()
class FamilyMemberOwnerException : Exception()