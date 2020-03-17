package com.wizaord.moneyweb.services

import com.nhaarman.mockitokotlin2.*
import com.wizaord.moneyweb.domain.transactions.Debit
import com.wizaord.moneyweb.services.fileparsers.FileParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class UploadFileServiceTest {

    @Mock
    lateinit var fileQifService: FileParser

    @Mock
    lateinit var familyBankAccountServiceFactory: FamilyBankAccountServiceFactory

    @Mock
    lateinit var familyBankAccountsService: FamilyBankAccountsService

    @InjectMocks
    lateinit var uploadFileService: UploadFileService

    @Test
    internal fun `loadFileForAccount - if file is qif extension, then call qif service file loader`() {
        // given
        val inputStream = this.javaClass.classLoader.getResourceAsStream("uploadFile/qif/00040745001 (7).qif")

        // when
        uploadFileService.loadFileForAccount("familyName", "accountName", "file.qif", inputStream!!)

        // then
        verify(fileQifService).parseFile(anyOrNull())
    }

    @Test
    internal fun `loadFileForAccount - if file is not qif extension, then do nothing`() {
        // given
        val inputStream = this.javaClass.classLoader.getResourceAsStream("uploadFile/qif/00040745001 (7).qif")

        // when
        uploadFileService.loadFileForAccount("familyName", "accountName", "file.ofx", inputStream!!)

        //then
        verifyZeroInteractions(fileQifService)
    }

    @Test
    internal fun `loadFileForAccount - after calling the specific parser, the transactions are loaded in familyAccount`() {
        // given
        val inputStream = this.javaClass.classLoader.getResourceAsStream("uploadFile/qif/00040745001 (7).qif")
        given(fileQifService.parseFile(anyOrNull())).willReturn(listOf(
                Debit("libelle", "bank", "desc", 10.0)
        ))
        given(familyBankAccountServiceFactory.getFamilyServiceWithTransactions(anyOrNull())).willReturn(familyBankAccountsService)
        given(familyBankAccountsService.transactionRegister(anyOrNull(), anyOrNull())).willAnswer { i -> i.arguments[1] }

        // when
        val nbInsert = uploadFileService.loadFileForAccount("familyName", "accountName", "file.qif", inputStream!!)

        // then
        assertThat(nbInsert).isEqualTo(AccountUploadResult("file.qif", 1, 1))
        verify(familyBankAccountsService).transactionRegister(eq("accountName"), anyOrNull())
    }
}