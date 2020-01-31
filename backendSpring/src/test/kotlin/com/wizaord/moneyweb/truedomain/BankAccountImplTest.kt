package com.wizaord.moneyweb.truedomain

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.wizaord.moneyweb.truedomain.transactions.Credit
import com.wizaord.moneyweb.truedomain.transactions.Debit
import com.wizaord.moneyweb.truedomain.transactions.ventilations.CreditVentilation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class BankAccountImplTest {

    @Mock
    lateinit var infrastructureBankAccountNotifications: InfrastructureBankAccountNotifications

    lateinit var bankAccountImpl : BankAccountImpl

    @BeforeEach
    internal fun beforeEach() {
        bankAccountImpl = BankAccountImpl("name", "bankName", infrastructureBankAccountNotifications)
    }

    @Test
    internal fun `constructor - When I create a account with mandatory param, I can read theses param`() {
        // given

        // when

        // then
        assertThat(bankAccountImpl.name).isEqualTo("name")
        assertThat(bankAccountImpl.bankName).isEqualTo("bankName")
    }

    @Test
    internal fun `constructor - When I create a bankAccount without date, the current day date is setted`() {
        // given

        // when

        // then
        assertThat(bankAccountImpl.dateCreation).isEqualTo(LocalDate.now())
    }

    @Test
    internal fun `constructor - When I create a bankAccount with a specific date, the dateCreation is this date`() {
        // given
        val oldDate = LocalDate.now().minusYears(3)

        // when
        val bankAccount = BankAccountImpl("name", "bankName", infrastructureBankAccountNotifications, oldDate)

        // then
        assertThat(bankAccount.dateCreation).isEqualTo(oldDate)
    }

    @Test
    internal fun `constructor - when BankAccount is created, solde is equal to 0`() {
        assertThat(bankAccountImpl.solde()).isEqualTo(0.0)
    }

    @Test
    internal fun `addTransaction - When I add a debit, the solde is decreased of the amount of the debit`() {
        // given
        val debit = Debit(10.0)

        // when
        bankAccountImpl.addTransaction(debit)

        // then
        assertThat(bankAccountImpl.solde()).isEqualTo(-10.0)
        verify(infrastructureBankAccountNotifications).notifyNewTransaction(debit)
    }


    @Test
    internal fun `addTransaction - When I add a credit, the solde is incresed of the amount of the credit`() {
        // given
        val credit = Credit(10.0)

        // when
        bankAccountImpl.addTransaction(credit)

        // then
        assertThat(bankAccountImpl.solde()).isEqualTo(10.0)
        verify(infrastructureBankAccountNotifications).notifyNewTransaction(credit)
    }

    @Test
    internal fun `getTransactionById - If the transaction is knew, then transaction is returned`() {
        // given
        val transaction = Credit(10.0)
        bankAccountImpl.addTransaction(transaction)

        // when
        val transactionById = bankAccountImpl.getTransactionById(transaction.id)

        // then
        assertThat(transactionById).isNotNull
        assertThat(transactionById).isEqualTo(transaction)
    }

    @Test
    internal fun `getTransactionById - If the transaction is not knew, then NoSuchElementException is raised`() {
        // given

        // when
        assertThrows(NoSuchElementException::class.java) {
            bankAccountImpl.getTransactionById("hello")
        }
    }

    @Test
    internal fun `removeTransaction - when I removeTransaction, the transactions is removed ^^`() {
        // given
        val transaction = Credit(10.0)
        bankAccountImpl.addTransaction(transaction)

        // when
        bankAccountImpl.removeTransaction(transaction)

        // then
        assertThrows(NoSuchElementException::class.java) {
            bankAccountImpl.getTransactionById(transaction.id)
        }
        verify(infrastructureBankAccountNotifications).notifyRemoveTransaction(transaction)
    }

    @Test
    internal fun `updateTransaction - When transaction is updated, infra is called`() {
        // given
        val transaction = Credit(10.0)
        bankAccountImpl.addTransaction(transaction)
        transaction.addVentilation(CreditVentilation(10.0))

        // when
        bankAccountImpl.updateTransaction(transaction)

        // then
        assertThat(bankAccountImpl.getTransactions()[0].ventilations).hasSize(1)

        verify(infrastructureBankAccountNotifications).notifyRemoveTransaction(anyOrNull())
        verify(infrastructureBankAccountNotifications, times(2)).notifyNewTransaction(transaction)
    }

}

