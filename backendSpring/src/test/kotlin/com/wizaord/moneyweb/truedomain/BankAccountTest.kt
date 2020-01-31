package com.wizaord.moneyweb.truedomain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class BankAccountTest {

    lateinit var bankAccount : BankAccount

    @BeforeEach
    internal fun beforeEach() {
        bankAccount = BankAccount("name", "bankName")
    }

    @Test
    internal fun `constructor - When I create a account with mandatory param, I can read theses param`() {
        // given

        // when

        // then
        assertThat(bankAccount.name).isEqualTo("name")
        assertThat(bankAccount.bankName).isEqualTo("bankName")
    }

    @Test
    internal fun `constructor - When I create a bankAccount without date, the current day date is setted`() {
        // given

        // when

        // then
        assertThat(bankAccount.dateCreation).isEqualTo(LocalDate.now())
    }

    @Test
    internal fun `constructor - When I create a bankAccount with a specific date, the dateCreation is this date`() {
        // given
        val oldDate = LocalDate.now().minusYears(3)

        // when
        val bankAccount = BankAccount("name", "bankName", oldDate)

        // then
        assertThat(bankAccount.dateCreation).isEqualTo(oldDate)
    }

    @Test
    internal fun `constructor - when BankAccount is created, solde is equal to 0`() {
        assertThat(bankAccount.solde()).isEqualTo(0.0)
    }

    @Test
    internal fun `addTransaction - When I add a debit, the solde is decreased of the amount of the debit`() {
        // given
        val debit = Debit(10.0)

        // when
        bankAccount.addTransaction(debit)

        // then
        assertThat(bankAccount.solde()).isEqualTo(-10.0)
    }


    @Test
    internal fun `addTransaction - When I add a credit, the solde is incresed of the amount of the credit`() {
        // given
        val credit = Credit(10.0)

        // when
        bankAccount.addTransaction(credit)

        // then
        assertThat(bankAccount.solde()).isEqualTo(10.0)
    }

    @Test
    internal fun `getTransactionById - If the transaction is knew, then transaction is returned`() {
        // given
        val transaction = Credit(10.0)
        bankAccount.addTransaction(transaction)

        // when
        val transactionById = bankAccount.getTransactionById(transaction.id)

        // then
        assertThat(transactionById).isNotNull
        assertThat(transactionById).isEqualTo(transaction)
    }

    @Test
    internal fun `getTransactionById - If the transaction is not knew, then NoSuchElementException is raised`() {
        // given

        // when
        assertThrows(NoSuchElementException::class.java) {
            bankAccount.getTransactionById("hello")
        }
    }

    @Test
    internal fun `removeTransaction - when I removeTransaction, the transactions is removed ^^`() {
        // given
        val transaction = Credit(10.0)
        bankAccount.addTransaction(transaction)

        // when
        bankAccount.removeTransaction(transaction)

        // then
        assertThrows(NoSuchElementException::class.java) {
            bankAccount.getTransactionById(transaction.id)
        }
    }
}
