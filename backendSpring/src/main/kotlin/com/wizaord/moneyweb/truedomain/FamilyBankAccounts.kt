package com.wizaord.moneyweb.truedomain

import lombok.extern.slf4j.Slf4j

@Slf4j
class FamilyBankAccounts(
        val familyName: String
) {

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

    fun accessToAccounts() = this.bankAccountsOwners
    fun accessToAccountsByBankname(bankName: String) = accessToAccounts().filter { it.bankAccount.bankName == bankName }
    fun accessToAccountByAccountName(accountName: String) = accessToAccounts().firstOrNull { it.bankAccount.name == accountName }

    fun registerFamilyMember(familyMember: FamilyMember) = familyMembers.add(familyMember)
    fun removeFamilyMember(familyMember: FamilyMember) = familyMembers.remove(familyMember)

}

data class FamilyMember(val username: String)

class BankAccountWithTheSameNameException : Exception()
class FamilyMemberNotKnowException : Exception()