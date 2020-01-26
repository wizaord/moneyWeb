package com.wizaord.moneyweb.truedomain

data class BankAccountOwners(
        val bankAccount: BankAccount,
        val owners: MutableList<FamilyMember>) {

    fun hasOwner(familyMember: FamilyMember) = this.owners.contains(familyMember)

    @Throws(AccountWithoutOwnerException::class)
    fun removeOwner(familyMember: FamilyMember) {
        if (!this.owners.contains(familyMember)) { return }
        if (this.owners.size == 1) throw AccountWithoutOwnerException()
        this.owners.remove(familyMember)
    }

}

class AccountWithoutOwnerException : Exception()