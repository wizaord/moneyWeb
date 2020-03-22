package com.wizaord.moneyweb.infrastructure

import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import com.wizaord.moneyweb.domain.transactions.Transaction

interface FamilyBankAccountPersistence {
    fun loadFamilyBankAccountByFamilyName(familyName: String): FamilyBankAccountsImpl?
    fun loadTransactionsFromAccount(accountInternalId: String): List<Transaction>
    fun initFamily(familyBankAccountsImpl: FamilyBankAccountsImpl): FamilyBankAccountsImpl
    fun updateFamily(familyBankAccountsImpl: FamilyBankAccountsImpl): FamilyBankAccountsImpl
    fun transactionCreate(accountInternalId: String, transaction: Transaction)
    fun transactionRemove(transaction: Transaction)
    fun transactionDeleteAll()
    fun familyBankAccountDeleteAll()
}