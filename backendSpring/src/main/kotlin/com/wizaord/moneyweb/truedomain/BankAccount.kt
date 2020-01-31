package com.wizaord.moneyweb.truedomain

import com.wizaord.moneyweb.truedomain.transactions.Transaction
import java.time.LocalDate


data class BankAccount(
        val name: String,
        val bankName: String,
        val dateCreation: LocalDate? = LocalDate.now()) {

    private val transactions = mutableListOf<Transaction>()

    fun solde(): Double {
        return transactions
                .map { it.amount }
                .sum()
    }

    fun addTransaction(transaction: Transaction) = this.transactions.add(transaction)
    fun removeTransaction(transaction: Transaction) = transactions.remove(transaction)

    @Throws(NoSuchElementException::class)
    fun getTransactionById(transactionId: String): Transaction = transactions.first { it.id == transactionId }

}

