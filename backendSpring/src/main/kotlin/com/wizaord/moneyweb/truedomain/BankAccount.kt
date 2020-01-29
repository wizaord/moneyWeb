package com.wizaord.moneyweb.truedomain

import java.time.LocalDate


data class BankAccount(
        val name: String,
        val bankName: String,
        val dateCreation: LocalDate? = LocalDate.now()) {

    private val transaction = mutableListOf<Transaction>()

    fun solde(): Double {
        return transaction
                .map { it.amount }
                .sum()
    }

    fun addTransaction(transaction: Transaction) {
        this.transaction.add(transaction)
    }

    @Throws(NoSuchElementException::class)
    fun getTransactionById(transactionId: String): Transaction {
        return transaction.first { it.id == transactionId }
    }
}

