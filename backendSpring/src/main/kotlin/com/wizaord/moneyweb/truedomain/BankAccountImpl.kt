package com.wizaord.moneyweb.truedomain

import com.wizaord.moneyweb.truedomain.transactions.Transaction
import java.time.LocalDate

data class BankAccountImpl(
        val name: String,
        val bankName: String,
        private val infrastructureBankAccountNotifications: InfrastructureBankAccountNotifications,
        val dateCreation: LocalDate? = LocalDate.now()) : BankAccount {

    private val transactions = mutableListOf<Transaction>()

    override fun solde(): Double {
        return transactions
                .map { it.amount }
                .sum()
    }

    override fun addTransaction(transaction: Transaction) {
        this.transactions.add(transaction)
        infrastructureBankAccountNotifications.notifyNewTransaction(transaction)
    }

    override fun removeTransaction(transaction: Transaction) {
        transactions.remove(transaction)
        infrastructureBankAccountNotifications.notifyRemoveTransaction(transaction)
    }

    @Throws(NoSuchElementException::class)
    override fun updateTransaction(transaction: Transaction) {
        val currentTransaction = this.getTransactionById(transaction.id)
        this.removeTransaction(currentTransaction)
        this.addTransaction(transaction)
    }

    override fun getTransactions(): List<Transaction> = transactions.toList()

    @Throws(NoSuchElementException::class)
    override fun getTransactionById(transactionId: String): Transaction = transactions.first { it.id == transactionId }


}

