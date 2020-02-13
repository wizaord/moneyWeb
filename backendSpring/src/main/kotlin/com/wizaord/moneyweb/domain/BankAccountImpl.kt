package com.wizaord.moneyweb.domain

import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.domain.transactions.TransactionMatch
import org.slf4j.LoggerFactory
import java.time.LocalDate

data class BankAccountImpl(
        val accountName: String,
        val bankDefinition: String,
        private var infrastructureBankAccountNotifications: InfrastructureBankAccountNotifications? = null,
        val dateCreation: LocalDate = LocalDate.now(),
        var isOpen: Boolean = true) : BankAccount {

    private val log = LoggerFactory.getLogger(BankAccountImpl::class.java)

    private val transactions = mutableListOf<Transaction>()

    fun registerInfrastructureBankAccountNotification(infrastructureBankAccountNotifications: InfrastructureBankAccountNotifications) {
        this.infrastructureBankAccountNotifications = infrastructureBankAccountNotifications
    }

    override fun solde(): Double {
        return transactions
                .map { it.amount }
                .sum()
    }

    override fun getName() = this.accountName
    override fun getBankName() = this.bankDefinition

    override fun open() {
        this.isOpen = true
    }

    override fun close() {
        this.isOpen = false
    }

    override fun addTransaction(transaction: Transaction) {
        log.info("Added transaction to account {}", this.accountName)
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

