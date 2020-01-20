package com.wizaord.moneyweb.init

import com.wizaord.moneyweb.domain.Transaction
import com.wizaord.moneyweb.domain.Ventilation
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.math.absoluteValue

@Component
class DebitCreditLoader() {


    fun readDebitCredits(): List<Transaction> {
        val records: MutableList<Transaction> = mutableListOf()
        File(javaClass.classLoader.getResource("init" + File.separator + "debitcredit.csv").file).forEachLine {
            val splitStr = it.split(";")
            records.add(transformCsvDebitCreditLineInTransaction(splitStr))
        }
        return records
    }

    fun transformCsvDebitCreditLineInTransaction(csvLine: List<String>): Transaction {
        val montantTransfere = csvLine[5].toDouble()
        val accountDestination = csvLine[6]
        val detailLibellebanque = csvLine.getOrElse(7) {""}
        val fromAccountId = when (montantTransfere >= 0) {
            false -> accountDestination
            true -> null
        }
        val toAccountId = when (montantTransfere >= 0) {
            false -> null
            true -> accountDestination
        }

        val isPointe = csvLine[3] == "1"

        return Transaction(
                csvLine[1],
                detailLibellebanque,
                csvLine[1],
                createDateFromString(csvLine[2].split(" ")[0]),
                createDateFromString(csvLine[4].split(" ")[0]),
                isPointe,
                fromAccountId,
                toAccountId,
                mutableListOf(Ventilation(montantTransfere.absoluteValue)),
                csvLine[0]
        )
    }

    fun createDateFromString(dateSrt: String): Date {
        return Date.from(LocalDate.parse(dateSrt).atStartOfDay(ZoneId.systemDefault()).toInstant())
    }
}