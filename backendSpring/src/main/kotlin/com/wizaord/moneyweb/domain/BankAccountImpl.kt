package com.wizaord.moneyweb.domain

import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.domain.transactions.TransactionMatch
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.util.*
import kotlin.NoSuchElementException

data class BankAccountImpl(
        val accountName: String,
        val bankDefinition: String,
        private var infrastructureBankAccountNotifications: InfrastructureBankAccountNotifications? = null,
        val dateCreation: LocalDate = LocalDate.now(),
        var isOpen: Boolean = true,
        var solde: Double = 0.0,
        val accountId: String = UUID.randomUUID().toString()) : BankAccount {

    private val log = LoggerFactory.getLogger(BankAccountImpl::class.java)
    private val transactions = mutableListOf<Transaction>()

    fun registerInfrastructureBankAccountNotification(infrastructureBankAccountNotifications: InfrastructureBankAccountNotifications) {
        this.infrastructureBankAccountNotifications = infrastructureBankAccountNotifications
    }

    override fun solde() = this.solde

    override fun getName() = this.accountName
    override fun getBankName() = this.bankDefinition

    override fun open() {
        this.isOpen = true
        infrastructureBankAccountNotifications?.notifyAccountUpdate(this)
    }

    override fun close() {
        this.isOpen = false
        infrastructureBankAccountNotifications?.notifyAccountUpdate(this)
    }

    override fun addTransaction(transaction: Transaction) {
        log.info("Added transaction to account {}", this.accountName)
        this.transactions.add(transaction)
        this.solde += transaction.amount
        infrastructureBankAccountNotifications?.notifyNewTransaction(this.accountId, transaction)
        infrastructureBankAccountNotifications?.notifyAccountUpdate(this)

    }

    override fun removeTransaction(transaction: Transaction) {
        this.solde -= transaction.amount
        transactions.remove(transaction)
        infrastructureBankAccountNotifications?.notifyRemoveTransaction(transaction)
        infrastructureBankAccountNotifications?.notifyAccountUpdate(this)
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

    override fun deleteAllTransactions() {
        val transactionToDelete = this.transactions.toList()
        transactionToDelete.forEach { transaction -> this.removeTransaction(transaction) }
    }

    override fun getInternalId() = this.accountId

    @Throws(NoSuchElementException::class)
    override fun getTransactionById(transactionId: String): Transaction = transactions.first { it.id == transactionId }


}

