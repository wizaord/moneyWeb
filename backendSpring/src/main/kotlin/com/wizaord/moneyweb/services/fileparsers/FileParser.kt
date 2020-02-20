package com.wizaord.moneyweb.services.fileparsers

import com.wizaord.moneyweb.domain.transactions.Transaction
import java.io.InputStream

interface FileParser {
    fun parseFile(inputStream: InputStream): List<Transaction>
}
