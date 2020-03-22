package com.wizaord.moneyweb.infrastructure.maria

import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.infrastructure.FamilyBankAccountPersistence
import com.wizaord.moneyweb.infrastructure.maria.domain.Family
import com.wizaord.moneyweb.infrastructure.maria.domain.FamilyBankAccount
import com.wizaord.moneyweb.infrastructure.maria.domain.FamilyBankAccountsRepositorySql
import com.wizaord.moneyweb.infrastructure.maria.domain.TransactionsRepositorySql
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
@Profile("maria")
class FamilyBankAccountPersistenceImpl(
        @Autowired val familyBankAccountsRepositorySql: FamilyBankAccountsRepositorySql,
        @Autowired val transactionsRespositorySql: TransactionsRepositorySql
) : FamilyBankAccountPersistence {

    private val logger = LoggerFactory.getLogger(FamilyBankAccountPersistenceImpl::class.java)

    @Transactional(readOnly = true)
    override fun loadFamilyBankAccountByFamilyName(familyName: String): FamilyBankAccountsImpl? {
        val familyFromRepository = familyBankAccountsRepositorySql.findById(familyName).map { it -> it.family!!.toDomain() }
        return when {
            familyFromRepository.isPresent -> familyFromRepository.get()
            else -> null
        }
    }

    @Transactional(readOnly = true)
    override fun loadTransactionsFromAccount(accountInternalId: String): List<Transaction> {
        logger.info("Loaded transaction for account with id {}", accountInternalId)
        val transactions = transactionsRespositorySql.findByAccountInternalId(accountInternalId)
        return transactions.map { it.toDomain() }
    }

    @Transactional(readOnly = true)
    override fun loadTransactionsFromAccounts(accountInternalIds: List<String>): Map<String, List<Transaction>> {
        val transactions = transactionsRespositorySql.findByAccountInternalIdIn(accountInternalIds)
        val groupedTransactions = transactions.groupBy { transaction -> transaction.accountInternalId }

        val result: MutableMap<String, List<Transaction>> = mutableMapOf()
        groupedTransactions.forEach { (accountName, transactions) -> result[accountName!!] = transactions.map { it.toDomain() } }
        return result
    }

    override fun initFamily(familyBankAccountsImpl: FamilyBankAccountsImpl): FamilyBankAccountsImpl {
        val family = Family(familyBankAccountsImpl.familyName, FamilyBankAccount.fromDomain(familyBankAccountsImpl))
        val familyPersisted = familyBankAccountsRepositorySql.save(family)
        return familyPersisted.family!!.toDomain()
    }

    override fun updateFamily(familyBankAccountsImpl: FamilyBankAccountsImpl): FamilyBankAccountsImpl {
        val familyToUpdate = FamilyBankAccount.fromDomain(familyBankAccountsImpl)
        val familyPersisted = familyBankAccountsRepositorySql.getOne(familyToUpdate.familyId!!)
        familyPersisted.family = familyToUpdate
        return familyBankAccountsRepositorySql.save(familyPersisted).family!!.toDomain()
    }

    override fun transactionCreate(accountInternalId: String, transaction: Transaction) {
        val tr = com.wizaord.moneyweb.infrastructure.maria.domain.Transaction.fromDomain(transaction)
        tr.accountInternalId = accountInternalId
        this.transactionsRespositorySql.save(tr)
    }

    override fun transactionRemove(transaction: Transaction) {
        transactionsRespositorySql.deleteById(transaction.id)
    }

    override fun transactionDeleteAll() {
        transactionsRespositorySql.deleteAll()
    }

    override fun familyBankAccountDeleteAll() {
        familyBankAccountsRepositorySql.deleteAll()
    }

}