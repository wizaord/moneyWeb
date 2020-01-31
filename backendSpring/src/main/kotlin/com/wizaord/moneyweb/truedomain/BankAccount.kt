package com.wizaord.moneyweb.truedomain

import com.wizaord.moneyweb.truedomain.transactions.Transaction


interface BankAccount {

    fun solde(): Double

    fun addTransaction(transaction: Transaction)
    fun removeTransaction(transaction: Transaction)
    @Throws(java.util.NoSuchElementException::class)
    fun updateTransaction(transaction: Transaction)


    @Throws(NoSuchElementException::class)
    fun getTransactionById(transactionId: String): Transaction
    fun getTransactions(): List<Transaction>

}

