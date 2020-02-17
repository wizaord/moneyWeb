package com.wizaord.moneyweb.infrastructure.domain

import com.wizaord.moneyweb.domain.BankAccountImpl
import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.util.*

@Document
class FamilyBankAccount(
        @Id val familyId: String,
        val familyMembers: List<FamilyMember>,
        val bankAccountsOwners: List<BankAccountOwners>
) {

    companion object {
        fun fromDomain(familyBankAccounts: FamilyBankAccountsImpl): FamilyBankAccount {
            return FamilyBankAccount(familyBankAccounts.familyName,
                    familyBankAccounts.getFamily().map { FamilyMember.fromDomain(it) },
                    familyBankAccounts.bankAccountsOwners.map { BankAccountOwners.fromDomain(it) })
        }
    }

    fun toDomain(): FamilyBankAccountsImpl {
        val familyBankAccountsImpl = FamilyBankAccountsImpl(familyId)
        familyMembers.forEach { familyBankAccountsImpl.registerFamilyMember(it.toDomain()) }
        bankAccountsOwners.forEach { familyBankAccountsImpl.registerAccount(
                it.account.toDomain(), it.owners.map { owner -> owner.toDomain() }) }
        return familyBankAccountsImpl
    }

}


data class FamilyMember(val username: String) {

    companion object {
        fun fromDomain(familyMember: com.wizaord.moneyweb.domain.FamilyMember): FamilyMember {
            return FamilyMember(familyMember.username)
        }
    }

    fun toDomain(): com.wizaord.moneyweb.domain.FamilyMember {
        return com.wizaord.moneyweb.domain.FamilyMember(username)
    }
}

data class BankAccountOwners(val owners: List<FamilyMember>,
                             val account: BankAccount) {

    companion object {
        fun fromDomain(bankAccountOwner: com.wizaord.moneyweb.domain.BankAccountOwners): BankAccountOwners {
            return BankAccountOwners(bankAccountOwner.getOwners().map { FamilyMember.fromDomain(it) },
                    BankAccount.fromDomain(bankAccountOwner.bankAccount as BankAccountImpl))
        }
    }
}

data class BankAccount(val name: String,
                       val bankName: String,
                       val dateCreation: LocalDate,
                       val isOpened: Boolean,
                       val amount: Double,
                       val internalId: String) {

    companion object {
        fun fromDomain(bankAccount: BankAccountImpl): BankAccount {
            return BankAccount(bankAccount.accountName,
                    bankAccount.bankDefinition,
                    bankAccount.dateCreation,
                    bankAccount.isOpen,
                    bankAccount.solde(),
                    bankAccount.accountId)
        }
    }

    fun toDomain(): BankAccountImpl {
        return BankAccountImpl(name, bankName, dateCreation = dateCreation, isOpen = isOpened, accountId = internalId, solde = amount)
    }

}