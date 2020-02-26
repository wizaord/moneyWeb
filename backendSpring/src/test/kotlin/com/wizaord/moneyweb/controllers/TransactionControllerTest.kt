package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.configuration.toLocalDate
import com.wizaord.moneyweb.domain.transactions.Credit
import com.wizaord.moneyweb.domain.transactions.ventilations.CreditVentilation
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class TransactionControllerTest {

    @Test
    internal fun `toDomain - Function must map controller Transaction in domain Transaction`() {
        // given
        val dateCreation = Date()
        val ventilations = listOf(
                Ventilation(5.0, "1"),
                Ventilation(5.0, "2")
        )
        val transaction = Transaction("id", 10.0, "user", "bank", null, true, dateCreation, ventilations)

        // when
        val transactionToDomain = transaction.toDomain()

        // then
        assertThat(transactionToDomain).isInstanceOf(Credit::class.java)
        assertThat(transactionToDomain.id).isEqualTo("id")
        assertThat(transactionToDomain.amount).isEqualTo(10.0)
        assertThat(transactionToDomain.userLibelle).isEqualTo("user")
        assertThat(transactionToDomain.bankLibelle).isEqualTo("bank")
        assertThat(transactionToDomain.bankDetail).isNull()
        assertThat(transactionToDomain.isPointe).isTrue()
        assertThat(transactionToDomain.dateCreation).isEqualTo(dateCreation.toLocalDate())
        assertThat(transactionToDomain.ventilations).hasSize(2)
    }
}
