package com.wizaord.moneyweb.infrastructure

import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import com.wizaord.moneyweb.domain.transactions.Transaction

interface FamilyBankAccountPersistence {
    fun loadFamilyBankAccountByFamilyName(familyName: String): FamilyBankAccountsImpl?
    fun initFamily(familyBankAccountsImpl: FamilyBankAccountsImpl): FamilyBankAccountsImpl
    fun updateFamily(familyBankAccountsImpl: FamilyBankAccountsImpl): FamilyBankAccountsImpl
    fun transactionCreate(transaction: Transaction)
}