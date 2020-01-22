package com.wizaord.moneyweb.truedomain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class BankAccountTest {

    @Test
    internal fun `When I create a account with mandatory param, I can read theses param`() {
        // given

        // when
        val bankAccount = BankAccount("name", "bankName")

        // then
        assertThat(bankAccount.name).isEqualTo("name")
        assertThat(bankAccount.bankName).isEqualTo("bankName")
    }

    @Test
    internal fun `When I create a bankAccount without date, the current day date is setted`() {
        // given

        // when
        val bankAccount = BankAccount("name", "bankName")

        // then
        assertThat(bankAccount.dateCreation).isEqualTo(LocalDate.now())
    }

    @Test
    internal fun `When I create a bankAccount with a specific date, the dateCreation is this date`() {
        // given
        val oldDate = LocalDate.now().minusYears(3)

        // when
        val bankAccount = BankAccount("name", "bankName", oldDate)

        // then
        assertThat(bankAccount.dateCreation).isEqualTo(oldDate)
    }


}