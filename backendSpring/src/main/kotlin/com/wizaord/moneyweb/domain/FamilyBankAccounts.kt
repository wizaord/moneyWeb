package com.wizaord.moneyweb.domain

import com.wizaord.moneyweb.domain.exceptions.BankAccountWithTheSameNameException
import com.wizaord.moneyweb.domain.exceptions.FamilyMemberAlreadyExistException
import com.wizaord.moneyweb.domain.exceptions.FamilyMemberNotKnowException
import com.wizaord.moneyweb.domain.exceptions.FamilyMemberOwnerException
import com.wizaord.moneyweb.domain.transactions.Transaction

interface FamilyBankAccounts {
    @Throws(BankAccountWithTheSameNameException::class, FamilyMemberNotKnowException::class)
    fun registerAccount(bankAccount: BankAccount, owners: List<FamilyMember>)

    @Throws(BankAccountWithTheSameNameException::class)
    fun registerAccount(bankAccount: BankAccount)

    fun changeBankAccountOwners(bankAccountId: String, owners: List<FamilyMember>)

    fun accessToAccounts(): List<BankAccountOwners>
    fun accessToAccountsByBankname(bankName: String): List<BankAccountOwners>
    fun accessToAccountsByFamilyMember(familyMember: FamilyMember): List<BankAccountOwners>
    fun accessToAccountByAccountName(accountName: String): BankAccountOwners?

    @Throws(FamilyMemberAlreadyExistException::class)
    fun registerFamilyMember(familyMember: FamilyMember)

    @Throws(FamilyMemberOwnerException::class)
    fun removeFamilyMember(familyMember: FamilyMember)
    fun getFamily(): List<FamilyMember>

    fun getCategoryConfiguredByPreviousFamilyTransaction(transaction: Transaction): Transaction?
}