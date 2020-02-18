package com.wizaord.moneyweb.domain

import com.wizaord.moneyweb.domain.exceptions.AccountWithoutOwnerException

data class BankAccountOwners(
        val bankAccount: BankAccount,
        private val owners: MutableList<FamilyMember>) {

    fun hasOwner(familyMember: FamilyMember) = this.owners.contains(familyMember)
    fun getOwners() = this.owners.toList()
    fun containBankAccountWithName(bankName: String) = this.bankAccount.getName() == bankName

    @Throws(AccountWithoutOwnerException::class)
    fun removeOwner(familyMember: FamilyMember) {
        if (!this.owners.contains(familyMember)) {
            return
        }
        if (this.owners.size == 1) throw AccountWithoutOwnerException()
        this.owners.remove(familyMember)
    }

    fun replaceOwners(owners: List<FamilyMember>) {
        this.owners.clear()
        this.owners.addAll(owners)
    }
}

