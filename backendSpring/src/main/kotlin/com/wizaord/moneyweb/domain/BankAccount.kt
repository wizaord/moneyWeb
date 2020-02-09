package com.wizaord.moneyweb.domain

import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.domain.transactions.TransactionMatch


interface BankAccount {

    fun solde(): Double
    fun getName(): String
    fun getBankName(): String

    fun open()
    fun close()

    fun addTransaction(transaction: Transaction)
    fun removeTransaction(transaction: Transaction)
    @Throws(java.util.NoSuchElementException::class)
    fun updateTransaction(transaction: Transaction)


    @Throws(NoSuchElementException::class)
    fun getTransactionById(transactionId: String): Transaction
    fun getTransactions(): List<Transaction>
    fun getTransactionsMatched(transaction: Transaction): List<TransactionMatch>

}

