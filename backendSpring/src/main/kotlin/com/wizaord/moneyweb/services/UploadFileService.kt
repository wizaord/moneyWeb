package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.services.fileparsers.FileParser
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
@Scope("prototype")
class UploadFileService(
        @Autowired private val fileParserQif : FileParser,
        @Autowired private val familyBankAccountServiceFactory: FamilyBankAccountServiceFactory
) {

    private val logger = LoggerFactory.getLogger(UploadFileService::class.java)

    fun loadFileForAccount(familyName: String, accountName: String, fileName: String, inputStream: InputStream): AccountUploadResult {
        val transactions = when (extractExtensionIfLowerCase(fileName)) {
            "qif" -> fileParserQif.parseFile(inputStream)
            else -> listOf()
        }
        val familyService = familyBankAccountServiceFactory.getFamilyServiceWithTransactions(familyName)

        val transactionRegistered = transactions
                .asSequence()
                .map { enrichUserLibelleTransaction(it, familyService)}
                .map { enrichCategoryTransaction(it, familyService)}
                .map { transaction -> familyService.transactionRegister(accountName, transaction) }
                .filter { it }
                .count()

        return AccountUploadResult(fileName, transactions.size, transactionRegistered)
    }

    private fun enrichCategoryTransaction(transaction: Transaction, familyService: FamilyBankAccountsService): Transaction {
        val transactionMatch = familyService.getLastTransactionWithBetterMatchScore(transaction)
        for ((index, ventilation) in transaction.ventilations.withIndex()) {
            ventilation.categoryId = transactionMatch?.ventilations?.getOrNull(index)?.categoryId
        }
        transaction.ventilations.forEach { logger.info("Enrich category to {}", it.categoryId) }
        return transaction
    }

    private fun enrichUserLibelleTransaction(transaction: Transaction, familyService: FamilyBankAccountsService): Transaction {
        val transactionMatch = familyService.getLastTransactionWithBetterMatchScore(transaction)
        if (transactionMatch != null) {
            logger.info("Find match transaction {}", transactionMatch)
        }
        transaction.userLibelle = transactionMatch?.userLibelle?:transaction.bankLibelle
        return transaction
    }


    private fun extractExtensionIfLowerCase(fileName: String) = fileName.toLowerCase().split(".").last().trim()

}


data class AccountUploadResult(
        val fileName: String,
        val transactionToRegistered: Int,
        val transactionReallyRegistered: Int
)
