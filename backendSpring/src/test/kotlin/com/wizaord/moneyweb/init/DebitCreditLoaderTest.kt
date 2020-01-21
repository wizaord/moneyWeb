package com.wizaord.moneyweb.init

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class DebitCreditLoaderTest {

    @InjectMocks
    lateinit var debitCreditLoader: DebitCreditLoader

    @Test
    fun `When CSV Debit credit Line is received then Transaction Object is created`() {
        // given
        val csvLine = listOf("149644","Solde initial","2010-01-08 00:00:00","1","2010-01-09 00:00:00","0.00","33")

        // when
        val transaction = debitCreditLoader.transformCsvDebitCreditLineInTransaction(csvLine)

        // then
        assertThat(transaction.id).isEqualTo("149644")
        assertThat(transaction.libelleBanque).isEqualTo("Solde initial")
        assertThat(transaction.libelleUser).isEqualTo("Solde initial")
        assertThat(transaction.dateReceptionBanque).hasDayOfMonth(8).hasMonth(1).hasYear(2010)
        assertThat(transaction.dateReceptionMoneyWeb).hasDayOfMonth(9).hasMonth(1).hasYear(2010)
        assertThat(transaction.isPointe).isTrue()
        assertThat(transaction.fromAccountId).isNull()
        assertThat(transaction.toAccountId).isEqualTo("33")
        assertThat(transaction.ventilations).hasSize(0)
    }

    @Test
    fun `when CSV Ventilation Line is received then Ventilation is added to Transaction`() {
        // given
        val csvLine = listOf("146338","-100.00","149644","NULL","33")

        // when
        val ventilation = debitCreditLoader.transformVentilationLine(csvLine)

        // then
        assertThat(ventilation.montant).isEqualTo(100.0)
    }
}