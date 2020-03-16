package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.*
import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.infrastructure.FamilyBankAccountPersistence
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
@Scope("prototype")
class FamilyBankAccountsService(
        @Autowired val familyBankAccountPersistence: FamilyBankAccountPersistence,
        @Autowired val categoryService: CategoryService) : InfrastructureBankAccountFamilyNotifications, InfrastructureBankAccountNotifications {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private var activatePersistence = false
    lateinit var familyBankAccounts: FamilyBankAccountsImpl

    fun loadFamilyBankFromPersistence(familyName: String) {
        logger.info("FamilyBankAccountService will be initialised with family : {}", familyName)
        familyBankAccounts = FamilyBankAccountsImpl(familyName, this)
        val familyBankAccounts = familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(familyName)
        if (familyBankAccounts != null) {
            updateFamilyBankAccount(familyBankAccounts)
        }
        logger.info("FamilyBankAccountService successfully initialised")
    }

    fun loadTransactionsFromPersistence() {
        domainNotificationDeActivation()
        // load transactions from accounts
        familyBankAccounts.bankAccountsOwners.forEach { bankAccountOwner ->
            val transactionsLoaded = familyBankAccountPersistence.loadTransactionsFromAccount(bankAccountOwner.bankAccount.getInternalId())
            transactionsLoaded.forEach { transaction -> bankAccountOwner.bankAccount.addTransaction(transaction) }
        }
        domainNotificationActivation()
    }

    private fun updateFamilyBankAccount(familyBankAccountsImpl: FamilyBankAccountsImpl) {
        domainNotificationDeActivation()
        familyBankAccountsImpl.getFamily().forEach {
            familyBankAccounts.registerFamilyMember(it)
        }
        familyBankAccounts.bankAccountsOwners.addAll(familyBankAccountsImpl.bankAccountsOwners)

        //registered account Notification
        familyBankAccounts.bankAccountsOwners.forEach { bankAccountOwner ->
            bankAccountOwner.bankAccount.registerInfrastructureBankAccountNotification(this)
        }
        domainNotificationActivation()
    }


    fun keepOnlyBankAccount(accountName: String) {
        domainNotificationDeActivation()
        this.familyBankAccounts.accessToAccounts()
                .filter { !it.containBankAccountWithName(accountName) }
                .forEach { this.familyBankAccounts.removeAccount(it.bankAccount.getName()) }
        domainNotificationActivation()
    }

    private fun domainNotificationDeActivation() {
        activatePersistence = false
    }

    private fun domainNotificationActivation() {
        activatePersistence = true
    }

    fun accountRegister(name: String,
                        bankName: String,
                        createDate: LocalDate = LocalDate.now(),
                        owners: List<String> = this.familyBankAccounts.getFamily().map { it.username }) {

        val newBankAccount = BankAccountImpl(name, bankName, this, createDate)
        familyBankAccounts.registerAccount(newBankAccount, owners.map { owner -> FamilyMember(owner) })
    }


    fun accountClose(bankAccountName: String) = this.familyBankAccounts.accessToAccountByAccountName(bankAccountName)?.bankAccount?.close()
    fun accountOpen(bankAccountName: String) = this.familyBankAccounts.accessToAccountByAccountName(bankAccountName)?.bankAccount?.open()

    fun accountDelete(accountName: String) = familyBankAccounts.removeAccount(accountName)

    fun bankAccounts(): List<BankAccountOwners> = this.familyBankAccounts.accessToAccounts()
    fun bankAccountsSortByLastTransactions(): List<BankAccountOwners> = this.familyBankAccounts.accessToAccountsSortedByLastTransaction()

    fun owners() = familyBankAccounts.getFamily()

    fun ownerCreate(newFamilyMember: FamilyMember): FamilyMember {
        familyBankAccounts.registerFamilyMember(newFamilyMember)
        return newFamilyMember
    }

    override fun notifyFamilyBankAccountUpdate(familyBankAccountsImpl: FamilyBankAccountsImpl) {
        if (activatePersistence) {
            this.familyBankAccountPersistence.updateFamily(familyBankAccountsImpl)
            logger.info("FamilyAccount has been updated")
        }
    }

    override fun notifyNewTransaction(accountInternalId: String, transaction: Transaction) {
        if (activatePersistence) {
            this.familyBankAccountPersistence.transactionCreate(accountInternalId, transaction)
            logger.info("new Transaction has been created")
        }
    }

    override fun notifyRemoveTransaction(transaction: Transaction) {
        if (activatePersistence) {
            this.familyBankAccountPersistence.transactionRemove(transaction)
            logger.info("Transaction has been deleted")
        }
    }

    override fun notifyAccountUpdate(accountImpl: BankAccountImpl) {
        if (activatePersistence) {
            this.notifyFamilyBankAccountUpdate(this.familyBankAccounts)
        }
    }

    fun transactionRegister(accountId: String, transaction: Transaction) = transactionRegister(accountId, transaction, true)
    fun transactionRegister(accountId: String, transaction: Transaction, activateDupDetection: Boolean = true): Boolean {
        logger.info("Registering a new transaction for account : $accountId")
        val bankAccount = this.familyBankAccounts.accessToAccountByAccountName(accountId)?.bankAccount
        if (activateDupDetection) {
            return if (bankAccount?.hasTransactionByProperties(transaction) == false) {
                bankAccount.addTransaction(transaction)
                true
            } else {
                logger.info("Transaction already registered in account")
                false
            }
        } else {
            bankAccount?.addTransaction(transaction)
        }
        return true
    }

    fun transactions(accountName: String): List<Transaction> {
        return this.familyBankAccounts.accessToAccountByAccountName(accountName)?.bankAccount?.getTransactions()
                ?: emptyList()
    }

    fun transactionUpdate(accountName: String, transactionId: String, transaction: Transaction) {
        if (transactionId != transaction.id) return
        if (!transaction.isValid()) return
        val isVirementToInternalAccount = transaction.ventilations
                .filter { categoryService.isVirementCategory(it.categoryId) }
                .map { categoryService.getAccountNameVirementDestination(it.categoryId!!) }
                .firstOrNull()

        if (isVirementToInternalAccount != null) {
            logger.info("New virement to another account => {}", isVirementToInternalAccount)
            this.transactionRegister(isVirementToInternalAccount, transaction.reverseTransaction())
        }
        this.familyBankAccounts.accessToAccountByAccountName(accountName)?.bankAccount?.updateTransaction(transaction)
    }

    fun transactionDelete(accountName: String, transactionId: String) {
        val transactionById = this.familyBankAccounts.accessToAccountByAccountName(accountName)?.bankAccount?.getTransactionById(transactionId)
        if (transactionById != null) {
            this.familyBankAccounts.accessToAccountByAccountName(accountName)?.bankAccount?.removeTransaction(transactionById)
        } else {
            logger.warn("Transaction with ID {} does not exist in account {}", transactionId, accountName)
        }
    }

    fun getLastTransactionWithBetterMatchScore(transaction: Transaction): Transaction? {
        val sortedBy = this.bankAccounts()
                .asSequence()
                .map { it.bankAccount.getTransactionsMatched(transaction) }
                .flatten()
                .filter { it.matchPoint != 0.0 }
                .sortedBy { it.transaction.dateCreation }
                .sortedBy { it.matchPoint }
                .toList()
        return sortedBy.map { it.transaction }
                .lastOrNull()

    }

    fun accountUpdateName(accountName: String, newAccountName: String) = this.familyBankAccounts.accessToAccountByAccountName(accountName)?.bankAccount?.updateName(newAccountName)
    fun accountUpdateBankName(accountName: String, newBankName: String) = this.familyBankAccounts.accessToAccountByAccountName(accountName)?.bankAccount?.updateBankName(newBankName)
    fun accountUpdateDateCreation(accountName: String, newAccountDate: LocalDate) = this.familyBankAccounts.accessToAccountByAccountName(accountName)?.bankAccount?.updateBankAccountDateCreate(newAccountDate)
    fun accountUpdateOwners(accountName: String, newOwners: List<FamilyMember>) = this.familyBankAccounts.changeBankAccountOwners(accountName, newOwners)
    fun bankAccount(accountName: String) = this.familyBankAccounts.accessToAccountByAccountName(accountName)


}