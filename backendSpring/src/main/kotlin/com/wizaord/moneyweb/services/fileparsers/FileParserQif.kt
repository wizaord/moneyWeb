package com.wizaord.moneyweb.services.fileparsers

import com.wizaord.moneyweb.domain.transactions.Credit
import com.wizaord.moneyweb.domain.transactions.Debit
import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.domain.transactions.ventilations.CreditVentilation
import com.wizaord.moneyweb.domain.transactions.ventilations.DebitVentilation
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
@Scope("prototype")
class FileParserQif: FileParser {

    private val transactions = mutableListOf<Transaction>()
    private var currentQIFLine: QIFLine = QIFLine()
    private var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")


    override fun parseFile(inputStream: InputStream): List<Transaction> {
        val reader = inputStream.reader()
        reader.readLines().forEach { line -> handleLine(line) }

        return transactions
    }

    private fun handleLine(line: String) {
        val firstChar = line.get(0)
        val lineWithoutFirstChar = line.substring(1, line.length)
        when (firstChar) {
            '!' -> return
            '^' -> {
                this.transactions.add(currentQIFLine.toTransaction())
                this.currentQIFLine = QIFLine()
            }
            'D' -> this.currentQIFLine.dateCreated = LocalDate.parse(lineWithoutFirstChar, formatter)
            'T' -> this.currentQIFLine.amount = lineWithoutFirstChar.replace(",", "").toDouble()
            'P' -> this.currentQIFLine.descBank = lineWithoutFirstChar
        }
    }

}

class QIFLine(
        var dateCreated: LocalDate = LocalDate.now(),
        var amount: Double = 0.0,
        var descBank: String = "") {

    fun toTransaction(): Transaction {
        val transaction = when (amount > 0) {
            true -> Credit("", descBank, null, amount, dateCreation = dateCreated)
            false -> Debit("", descBank, null, amount, dateCreation = dateCreated)
        }
        val ventilation = when (amount > 0) {
            true -> CreditVentilation(amount)
            else -> DebitVentilation(amount)
        }
        transaction.addVentilation(ventilation)
        return transaction
    }
}