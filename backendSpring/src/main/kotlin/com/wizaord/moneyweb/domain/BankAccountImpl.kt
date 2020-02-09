package com.wizaord.moneyweb.domain

import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.domain.transactions.TransactionMatch
import java.time.LocalDate

data class BankAccountImpl(
        val accountName: String,
        val bankDefinition: String,
        private val infrastructureBankAccountNotifications: InfrastructureBankAccountNotifications?,
        val dateCreation: LocalDate = LocalDate.now()) : BankAccount {

    private val transactions = mutableListOf<Transaction>()

    override fun solde(): Double {
        return transactions
                .map { it.amount }
                .sum()
    }

    override fun getName() = this.accountName
    override fun getBankName() = this.bankDefinition

    override fun addTransaction(transaction: Transaction) {
        this.transactions.add(transaction)
        infrastructureBankAccountNotifications?.notifyNewTransaction(transaction)
    }

    override fun removeTransaction(transaction: Transaction) {
        transactions.remove(transaction)
        infrastructureBankAccountNotifications?.notifyRemoveTransaction(transaction)
    }

    @Throws(NoSuchElementException::class)
    override fun updateTransaction(transaction: Transaction) {
        val currentTransaction = this.getTransactionById(transaction.id)
        this.removeTransaction(currentTransaction)
        this.addTransaction(transaction)
    }

    override fun getTransactions(): List<Transaction> = transactions.toList()
    override fun getTransactionsMatched(transaction: Transaction): List<TransactionMatch> {
        return this.transactions.map { TransactionMatch(it, it.matchWith(transaction)) }
    }

    @Throws(NoSuchElementException::class)
    override fun getTransactionById(transactionId: String): Transaction = transactions.first { it.id == transactionId }


}

