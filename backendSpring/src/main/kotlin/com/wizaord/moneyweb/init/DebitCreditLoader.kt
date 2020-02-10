package com.wizaord.moneyweb.init

import com.wizaord.moneyweb.domain.transactions.Credit
import com.wizaord.moneyweb.domain.transactions.Debit
import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.services.FamilyBankAccountServiceFactory
import com.wizaord.moneyweb.services.FamilyBankAccountsService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDate

@Component
class DebitCreditLoader(
        @Autowired var familyBankAccountServiceFactory: FamilyBankAccountServiceFactory
) {

    private val logger = LoggerFactory.getLogger(DebitCreditLoader::class.java)

    @Value("\${moneyweb.init.initdatabase.fileLocation.transactions}")
    lateinit var transactionsFilePath: String

    @Value("\${moneyweb.init.initdatabase.fileLocation.transactionsDetails}")
    lateinit var transactionDetailsFilePath: String

    private lateinit var serviceBeanForFamily: FamilyBankAccountsService


    fun loadFamilyBankAccount(familyName: String) {
        serviceBeanForFamily = familyBankAccountServiceFactory.getServiceBeanForFamily(familyName)
    }

    fun loadDebitCredit(accountsMap: Map<String, String>) {
        File(transactionsFilePath)
                .forEachLine {
                    val splitStr = it.split(";").map { splitedChar -> splitedChar.replace("\"", "") }
                    transformCsvDebitCreditLineInTransactionAndAddToAccount(splitStr, accountsMap)
                }
    }


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

//    fun transformVentilationLine(splitStr: List<String>): Ventilation {
//        val categorieId= splitStr[3]
//        return Ventilation(splitStr[1].toDouble().absoluteValue,
//                categorieId)
//    }


    //"149645";"BP LEP";"2010-01-08 00:00:00";"1";"2010-01-08 00:00:00";"7700.00";"33";"SOUSCRIPTION LIVRET EPARG"
    fun transformCsvDebitCreditLineInTransactionAndAddToAccount(csvLine: List<String>, accountsMap: Map<String, String>) {
        val montantTransfere = csvLine[5].replace("\"", "").toDouble()
        val accountDestination = csvLine[6]
        val detailLibellebanque = csvLine.getOrElse(7) { "" }
        val isCredit = (montantTransfere >= 0)

        val isPointe = csvLine[3] == "1"

        var transaction: Transaction?
        if (isCredit) {
            transaction = Credit(csvLine[1], detailLibellebanque, csvLine[1], 0.0, isPointe, csvLine[0])
        } else {
            transaction = Debit(csvLine[1], detailLibellebanque, csvLine[1], 0.0, isPointe, csvLine[0])
        }
        transaction.dateCreation = createDateFromString(csvLine[2].split(" ")[0])!!

        // search account by Id
        serviceBeanForFamily.transactionRegister(accountsMap[csvLine[6]]!!, transaction)

    }

    fun createDateFromString(dateSrt: String): LocalDate? {
        if (dateSrt == "NULL") return null
        return LocalDate.parse(dateSrt)
    }
//
//    fun loadEveythingInMongo() {
//        transactions.forEach { transaction ->
//            transactionRepository.insert(transaction)
//        }
//    }

}
//
//data class Transaction(val libelleUser: String,
//                       val libelleBanque: String,
//                       val dateCreation: Date,
//                       val datePointage: Date?,
//                       val isPointe: Boolean,
//                       val fromAccount: String?,
//                       val toAccount: String?,
//                       val id: String) {
//    val ventilations = mutableListOf<Ventilation>()
//
//}
//
//data class Ventilation(val montant: Double,
//                       val categoryId: String) {
//
//}