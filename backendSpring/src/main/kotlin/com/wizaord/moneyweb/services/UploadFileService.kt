package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.services.fileparsers.FileParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
@Scope("prototype")
class UploadFileService(
        @Autowired private val fileParserQif : FileParser,
        @Autowired private val familyBankAccountServiceFactory: FamilyBankAccountServiceFactory
) {

    fun loadFileForAccount(familyName: String, accountName: String, fileName: String, inputStream: InputStream) {
        val transactions = when (extractExtensionIfLowerCase(fileName)) {
            "qif" -> fileParserQif.parseFile(inputStream)
            else -> listOf()
        }
        val familyService = familyBankAccountServiceFactory.getFamilyServiceWithTransactions(familyName)
        transactions.forEach { transaction -> familyService.transactionRegister(accountName, transaction) }
    }

    private fun extractExtensionIfLowerCase(fileName: String) = fileName.toLowerCase().split(".").last().trim()

}
