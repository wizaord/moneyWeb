package com.wizaord.moneyweb.domain

import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.domain.transactions.TransactionMatch
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.util.*
import kotlin.NoSuchElementException

data class BankAccountImpl(
        var accountName: String,
        var bankDefinition: String,
        private var infrastructureBankAccountNotifications: InfrastructureBankAccountNotifications? = null,
        var dateCreation: LocalDate = LocalDate.now(),
        var isOpen: Boolean = true,
        var solde: Double = 0.0,
        val accountId: String = UUID.randomUUID().toString()) : BankAccount {

    private val log = LoggerFactory.getLogger(BankAccountImpl::class.java)
    private val transactions = mutableListOf<Transaction>()

    fun registerInfrastructureBankAccountNotification(infrastructureBankAccountNotifications: InfrastructureBankAccountNotifications?) {
        this.infrastructureBankAccountNotifications = infrastructureBankAccountNotifications
    }

    override fun solde() = this.solde

    override fun getName() = this.accountName
    override fun getBankName() = this.bankDefinition

    override fun open() {
        this.isOpen = true
        notifyAccountUpdated()
    }

    override fun close() {
        this.isOpen = false
        notifyAccountUpdated()
    }

    override fun addTransaction(transaction: Transaction) {
        log.debug("Added transaction to account {}", this.accountName)
        this.transactions.add(transaction)
        this.solde += transaction.amount
        infrastructureBankAccountNotifications?.notifyNewTransaction(this.accountId, transaction)
        notifyAccountUpdated()

    }

    override fun removeTransaction(transaction: Transaction) {
        this.solde -= transaction.amount
        transactions.remove(transaction)
        infrastructureBankAccountNotifications?.notifyRemoveTransaction(transaction)
        notifyAccountUpdated()
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

    override fun updateName(newAccountName: String) {
        accountName = newAccountName
        notifyAccountUpdated()
    }

    private fun notifyAccountUpdated() {
        this.infrastructureBankAccountNotifications?.notifyAccountUpdate(this)
    }

    override fun updateBankName(newBankDetail: String) {
        bankDefinition = newBankDetail
        notifyAccountUpdated()
    }

    override fun updateBankAccountDateCreate(newAccountDate: LocalDate) {
        dateCreation = newAccountDate
        notifyAccountUpdated()
    }

    override fun getLastTransaction(): Transaction? {
        return this.transactions
                .sortedBy { transaction -> transaction.dateCreation }
                .lastOrNull()
    }

    @Throws(NoSuchElementException::class)
    override fun getTransactionById(transactionId: String): Transaction = transactions.first { it.id == transactionId }


}

