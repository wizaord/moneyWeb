package com.wizaord.moneyweb.services.fileparsers

import com.wizaord.moneyweb.domain.transactions.Debit
import com.wizaord.moneyweb.domain.transactions.ventilations.DebitVentilation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class FileParserQifTest {

    @InjectMocks
    lateinit var fileParserQif: FileParserQif

    @Test
    internal fun `parseFile - check that file are successfully parsed`() {
        // given
        val inputStream = this.javaClass.classLoader.getResourceAsStream("uploadFile/qif/00040745001 (7).qif")
        val libelleBank = "CARTE 18/01/20 38 ALINEA 04      CB*9972"
        val dateCreation = LocalDate.of(2020, 1, 20)
        // when
        val transactions = fileParserQif.parseFile(inputStream!!)

        // then
        assertThat(transactions).hasSize(6)
        transactions.forEach { assertThat(it.isValid()) }

        val transaction1 = transactions[0]
        assertThat(transaction1).isInstanceOf(Debit::class.java)
        assertThat(transaction1.amount).isEqualTo(-12.40)
        assertThat(transaction1.userLibelle).isEqualTo(libelleBank)
        assertThat(transaction1.bankLibelle).isEqualTo(libelleBank)
        assertThat(transaction1.bankDetail).isNull()
        assertThat(transaction1.dateCreation).isEqualTo(dateCreation)
        assertThat(transaction1.isPointe).isFalse()

    }
}

