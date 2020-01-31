package com.wizaord.moneyweb.truedomain

import com.wizaord.moneyweb.truedomain.exceptions.AccountWithoutOwnerException

data class BankAccountOwners(
        val bankAccountImpl: BankAccountImpl,
        private val owners: MutableList<FamilyMember>) {

    fun hasOwner(familyMember: FamilyMember) = this.owners.contains(familyMember)
    fun getOwners() = this.owners.toList()

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

