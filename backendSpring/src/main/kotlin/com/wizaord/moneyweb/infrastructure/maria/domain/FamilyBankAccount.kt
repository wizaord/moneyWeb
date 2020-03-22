package com.wizaord.moneyweb.infrastructure.maria.domain

import com.wizaord.moneyweb.configuration.toDate
import com.wizaord.moneyweb.configuration.toLocalDate
import com.wizaord.moneyweb.domain.BankAccountImpl
import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import java.util.*
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "Families")
class Family(
    @Id
    var familyName: String? = null,
    @Convert(converter = FamilyConverterJson::class)
    @Column(columnDefinition = "longtext")
    var family: FamilyBankAccount? = null) {

}

class FamilyBankAccount(
        var familyId: String? = null,
        var familyMembers: List<FamilyMember> = listOf(),
        var bankAccountsOwners: List<BankAccountOwners> = listOf()
) {

    companion object {
        fun fromDomain(familyBankAccounts: FamilyBankAccountsImpl): FamilyBankAccount {
            return FamilyBankAccount(familyBankAccounts.familyName,
                    familyBankAccounts.getFamily().map { FamilyMember.fromDomain(it) }.toList(),
                    familyBankAccounts.bankAccountsOwners.map { BankAccountOwners.fromDomain(it) }.toList())
        }
    }

    fun toDomain(): FamilyBankAccountsImpl {
        val familyBankAccountsImpl = FamilyBankAccountsImpl(familyId!!)
        familyMembers.forEach { familyBankAccountsImpl.registerFamilyMember(it.toDomain()) }
        bankAccountsOwners.forEach {
            familyBankAccountsImpl.registerAccount(
                    it.account!!.toDomain(), it.owners.map { owner -> owner.toDomain() })
        }
        return familyBankAccountsImpl
    }
}

data class FamilyMember(
        var username: String? = null) {

    companion object {
        fun fromDomain(familyMember: com.wizaord.moneyweb.domain.FamilyMember): FamilyMember {
            return FamilyMember(familyMember.username)
        }
    }

    fun toDomain(): com.wizaord.moneyweb.domain.FamilyMember {
        return com.wizaord.moneyweb.domain.FamilyMember(username!!)
    }

}

data class BankAccountOwners(
        var owners: List<FamilyMember> = listOf(),
        var account: BankAccount? = null) {

    companion object {
        fun fromDomain(bankAccountOwner: com.wizaord.moneyweb.domain.BankAccountOwners): BankAccountOwners {
            return BankAccountOwners(bankAccountOwner.getOwners().map { FamilyMember.fromDomain(it) }.toList(),
                    BankAccount.fromDomain(bankAccountOwner.bankAccount as BankAccountImpl))
        }
    }
}

data class BankAccount(
        var name: String? = null,
        var bankName: String? = null,
        var dateCreation: Date? = null,
        var isOpened: Boolean? = null,
        var amount: Double? = null,
        var internalId: String? = null) {

    companion object {
        fun fromDomain(bankAccount: BankAccountImpl): BankAccount {
            return BankAccount(bankAccount.accountName,
                    bankAccount.bankDefinition,
                    bankAccount.dateCreation.toDate(),
                    bankAccount.isOpen,
                    bankAccount.solde(),
                    bankAccount.accountId)
        }
    }
    fun toDomain(): BankAccountImpl {
        return BankAccountImpl(name!!, bankName!!, dateCreation = dateCreation!!.toLocalDate(), isOpen = isOpened!!, accountId = internalId!!, solde = amount!!)
    }
}
