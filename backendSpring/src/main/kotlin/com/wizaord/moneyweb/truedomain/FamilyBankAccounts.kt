package com.wizaord.moneyweb.truedomain

import com.wizaord.moneyweb.truedomain.categories.Category
import com.wizaord.moneyweb.truedomain.exceptions.BankAccountWithTheSameNameException
import com.wizaord.moneyweb.truedomain.exceptions.FamilyMemberAlreadyExistException
import com.wizaord.moneyweb.truedomain.exceptions.FamilyMemberNotKnowException
import com.wizaord.moneyweb.truedomain.exceptions.FamilyMemberOwnerException
import com.wizaord.moneyweb.truedomain.transactions.Transaction

interface FamilyBankAccounts {
    @Throws(BankAccountWithTheSameNameException::class, FamilyMemberNotKnowException::class)
    fun registerAccount(bankAccountImpl: BankAccountImpl, owners: List<FamilyMember>)

    @Throws(BankAccountWithTheSameNameException::class)
    fun registerAccount(bankAccountImpl: BankAccountImpl)

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

//    fun getCategoryConfiguredByPreviousFamilyTransaction(transaction: Transaction): Category?
}