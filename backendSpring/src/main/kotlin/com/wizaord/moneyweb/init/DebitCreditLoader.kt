package com.wizaord.moneyweb.init

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.math.absoluteValue
//
//@Component
//class DebitCreditLoader(
//        @Autowired var transactionRepository: TransactionRepository
//) {
//
//    private val logger = LoggerFactory.getLogger(DebitCreditLoader::class.java)
//
//    @Value("\${moneyweb.init.initdatabase.fileLocation.transactions}")
//    lateinit var transactionsFilePath: String
//
//    @Value("\${moneyweb.init.initdatabase.fileLocation.transactionsDetails}")
//    lateinit var transactionDetailsFilePath: String
//
//    private val transactions = mutableListOf<Transaction>()
//
//    fun loadDebitCredit() {
//        File(transactionsFilePath)
//                .forEachLine {
//                    val splitStr = it.split(";")
//                            .map { splitedChar -> splitedChar.replace("\"", "") }
//                    transactions.add(transformCsvDebitCreditLineInTransaction(splitStr))
//                }
//    }
//
//
//    fun loadDetailMontant() {
//        File(transactionDetailsFilePath)
//                .forEachLine {
//                    val splitStr = it.split(";")
//                            .map { splitedChar -> splitedChar.replace("\"", "") }
//                    val transactionId = splitStr[2]
//
//                    val transaction = transactions
//                            .first { transaction -> transaction.id == transactionId }
//
//                    // add ventilation
//                    transaction.ventilations.add(transformVentilationLine(splitStr))
//
//                    // if virement interne replace from or to
//                    // FIXME : finir implementation ici
//                }
//        logger.info("All transactions have been loaded")
//    }
//
//    fun transformVentilationLine(splitStr: List<String>): Ventilation {
//        val categorieId= splitStr[3]
//        return Ventilation(splitStr[1].toDouble().absoluteValue,
//                categorieId)
//    }
//
//    fun transformCsvDebitCreditLineInTransaction(csvLine: List<String>): Transaction {
//        val montantTransfere = csvLine[5].replace("\"", "").toDouble()
//        val accountDestination = csvLine[6]
//        val detailLibellebanque = csvLine.getOrElse(7) { "" }
//        val fromAccountId = when (montantTransfere >= 0) {
//            false -> accountDestination
//            true -> null
//        }
//        val toAccountId = when (montantTransfere >= 0) {
//            false -> null
//            true -> accountDestination
//        }
//
//        val isPointe = csvLine[3] == "1"
//
//        return Transaction(
//                csvLine[1],
//                detailLibellebanque,
//                csvLine[1],
//                createDateFromString(csvLine[2].split(" ")[0])!!,
//                createDateFromString(csvLine[4].split(" ")[0]),
//                isPointe,
//                fromAccountId,
//                toAccountId,
//                mutableListOf(),
//                csvLine[0]
//        )
//    }
//
//    fun createDateFromString(dateSrt: String): Date? {
//        if (dateSrt == "NULL") return null
//        return Date.from(LocalDate.parse(dateSrt).atStartOfDay(ZoneId.systemDefault()).toInstant())
//    }
//
//    fun loadEveythingInMongo() {
//        transactions.forEach { transaction ->
//            transactionRepository.insert(transaction)
//        }
//    }
//
//}