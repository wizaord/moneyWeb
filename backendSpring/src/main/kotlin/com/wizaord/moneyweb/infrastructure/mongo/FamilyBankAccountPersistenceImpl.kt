package com.wizaord.moneyweb.infrastructure.mongo

import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import com.wizaord.moneyweb.domain.transactions.Credit
import com.wizaord.moneyweb.domain.transactions.Debit
import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.infrastructure.FamilyBankAccountPersistence
import com.wizaord.moneyweb.infrastructure.mongo.domain.FamilyBankAccount
import com.wizaord.moneyweb.infrastructure.mongo.domain.FamilyBankAccountsRepository
import com.wizaord.moneyweb.infrastructure.mongo.domain.TransactionsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
@Profile("mongo")
class FamilyBankAccountPersistenceImpl(
        @Autowired val familyBankAccountsRepository: FamilyBankAccountsRepository,
        @Autowired val transactionsRepository: TransactionsRepository
) : FamilyBankAccountPersistence {

    private val logger = LoggerFactory.getLogger(FamilyBankAccountPersistenceImpl::class.java)

    override fun loadFamilyBankAccountByFamilyName(familyName: String): FamilyBankAccountsImpl? {
        val familyFromRepository = familyBankAccountsRepository.findById(familyName).map { it -> it.toDomain() }
        return when {
            familyFromRepository.isPresent -> familyFromRepository.get()
            else -> null
        }
    }

    override fun loadTransactionsFromAccount(accountInternalId: String): List<Transaction> {
        logger.info("Loaded transaction for account with id {}", accountInternalId)
        val transactions = transactionsRepository.findByAccountInternalId(accountInternalId)
        return transactions.map { it.toDomain() }
    }

    override fun initFamily(familyBankAccountsImpl: FamilyBankAccountsImpl): FamilyBankAccountsImpl {
        return familyBankAccountsRepository.save(FamilyBankAccount.fromDomain(familyBankAccountsImpl)).toDomain()
    }

    override fun updateFamily(familyBankAccountsImpl: FamilyBankAccountsImpl): FamilyBankAccountsImpl {
        val familyToUpdate = FamilyBankAccount.fromDomain(familyBankAccountsImpl)
        return familyBankAccountsRepository.save(familyToUpdate).toDomain()
    }

    override fun transactionCreate(accountInternalId: String, transaction: Transaction) {
        lateinit var tr: com.wizaord.moneyweb.infrastructure.mongo.domain.Transaction
        when (transaction) {
            is Credit -> tr = com.wizaord.moneyweb.infrastructure.mongo.domain.Credit.fromDomain(transaction)
            is Debit -> tr = com.wizaord.moneyweb.infrastructure.mongo.domain.Debit.fromDomain(transaction)
        }
        tr.accountInternalId = accountInternalId
        logger.info("Persist new transaction with ID ${tr.id}")
        this.transactionsRepository.save(tr)
    }

    override fun transactionRemove(transaction: Transaction) {
        transactionsRepository.deleteById(transaction.id)
    }

    override fun transactionDeleteAll() {
        transactionsRepository.deleteAll()
    }

    override fun familyBankAccountDeleteAll() {
        familyBankAccountsRepository.deleteAll()
    }

}