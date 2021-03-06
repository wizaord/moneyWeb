package com.wizaord.moneyweb.domain

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.wizaord.moneyweb.domain.transactions.Credit
import com.wizaord.moneyweb.domain.transactions.Debit
import com.wizaord.moneyweb.domain.transactions.TransactionMatch
import com.wizaord.moneyweb.domain.transactions.ventilations.CreditVentilation
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

    lateinit var bankAccount: BankAccountImpl

    @BeforeEach
    internal fun beforeEach() {
        bankAccount = BankAccountImpl("name", "bankName", infrastructureBankAccountNotifications)
    }

    @Test
    internal fun `constructor - When I create a account with mandatory param, I can read theses param`() {
        // given

        // when

        // then
        assertThat(bankAccount.accountName).isEqualTo("name")
        assertThat(bankAccount.bankDefinition).isEqualTo("bankName")
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
        val bankAccount = BankAccountImpl("name", "bankName", infrastructureBankAccountNotifications, oldDate)

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
        val debit = Debit("lib", "libBank", "desc", 10.0)

        // when
        bankAccount.addTransaction(debit)

        // then
        assertThat(bankAccount.solde()).isEqualTo(-10.0)
        verify(infrastructureBankAccountNotifications).notifyNewTransaction(anyOrNull(), anyOrNull())
    }


    @Test
    internal fun `addTransaction - When I add a credit, the solde is incresed of the amount of the credit`() {
        // given
        val credit = Credit("lib", "libBank", "desc", 10.0)

        // when
        bankAccount.addTransaction(credit)

        // then
        assertThat(bankAccount.solde()).isEqualTo(10.0)
        verify(infrastructureBankAccountNotifications).notifyNewTransaction(anyOrNull(), anyOrNull())
    }

    @Test
    internal fun `getTransactionById - If the transaction is knew, then transaction is returned`() {
        // given
        val transaction = Credit("lib", "libBank", "desc", 10.0)
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
        val transaction = Credit("lib", "libBank", "desc", 10.0)
        bankAccount.addTransaction(transaction)

        // when
        bankAccount.removeTransaction(transaction)

        // then
        assertThrows(NoSuchElementException::class.java) {
            bankAccount.getTransactionById(transaction.id)
        }
        verify(infrastructureBankAccountNotifications).notifyRemoveTransaction(transaction)
    }

    @Test
    internal fun `updateTransaction - When transaction is updated, infra is called`() {
        // given
        val transaction = Credit("lib", "libBank", "desc", 10.0)
        transaction.addVentilation(CreditVentilation(10.0))
        bankAccount.addTransaction(transaction)

        // when
        bankAccount.updateTransaction(transaction)

        // then
        assertThat(bankAccount.getTransactions()[0].ventilations).hasSize(1)

        verify(infrastructureBankAccountNotifications).notifyRemoveTransaction(anyOrNull())
        verify(infrastructureBankAccountNotifications, times(2)).notifyNewTransaction(anyOrNull(), anyOrNull())
    }

    @Test
    internal fun `getTransactionMatched - transaction with the same userlibelle, return 3,0`() {
        // given
        val transaction = Credit("lib", "libBank", "desc", 10.0)
        bankAccount.addTransaction(transaction)

        // when
        val transactionsMatched = bankAccount.getTransactionsMatched(transaction)

        //then
        assertThat(transactionsMatched).hasSize(1)
        assertThat(transactionsMatched[0]).isEqualTo(TransactionMatch(transaction, 3.0))
    }

    @Test
    internal fun `open - when I open a bankAccount the bank accout is opened`() {
        // given
        bankAccount.isOpen = false;

        // when
        bankAccount.open()

        // then
        assertThat(bankAccount.isOpen).isTrue()
    }

    @Test
    internal fun `close - when I close a bankAccount the bank accout is closed`() {
        // given
        bankAccount.isOpen = true;

        // when
        bankAccount.close()

        // then
        assertThat(bankAccount.isOpen).isFalse()
    }

    @Test
    internal fun `constructor - When BankAccount is created, an internal Id is created`() {
        assertThat(bankAccount.accountId).isNotNull()
    }

    @Test
    internal fun updateName() {
        // when
        bankAccount.updateName("PLOP")

        // then
        assertThat(bankAccount.accountName).isEqualTo("PLOP")
        verify(infrastructureBankAccountNotifications).notifyAccountUpdate(anyOrNull())
    }

    @Test
    internal fun updateBankName() {
        bankAccount.updateBankName("PLOP")
        assertThat(bankAccount.bankDefinition)
        verify(infrastructureBankAccountNotifications).notifyAccountUpdate(anyOrNull())
    }

    @Test
    internal fun dateCreateUpdate() {
        val now = LocalDate.now()
        bankAccount.updateBankAccountDateCreate(now)
        assertThat(bankAccount.dateCreation).isEqualTo(now)
        verify(infrastructureBankAccountNotifications).notifyAccountUpdate(anyOrNull())
    }

    @Test
    internal fun getLastTransaction() {
        // given
        bankAccount.addTransaction(Credit("lib", "libBank", "desc", 10.0, dateCreation = LocalDate.of(2020, 1, 1)))
        bankAccount.addTransaction(Credit("lib", "libBank", "desc", 10.0, dateCreation = LocalDate.of(2020, 1, 2)))
        bankAccount.addTransaction(Credit("lib", "libBank", "desc", 10.0, dateCreation = LocalDate.of(2020, 1, 3)))

        // when
        val lastTransaction = bankAccount.getLastTransaction()

        // then
        assertThat(lastTransaction).isNotNull
        assertThat(lastTransaction!!.dateCreation).isEqualTo(LocalDate.of(2020, 1, 3))
    }

    @Test
    internal fun getLastTransactionWithoutTransaction() {
        // given
        // when
        val lastTransaction = bankAccount.getLastTransaction()

        // then
        assertThat(lastTransaction).isNull()
    }

    @Test
    internal fun `hasTransactionByProperties - if transaction as the same date, amount and blankLibelle then return true`() {
        // given
        val transaction = Credit("userLibelle", "BandLibelle", "", 10.0)
        bankAccount.addTransaction(transaction)

        // when
        val hasTransactionByProperties = bankAccount.hasTransactionByProperties(transaction)

        // then
        assertThat(hasTransactionByProperties).isTrue()
    }

    @Test
    internal fun `soldeRefresh - check debit and credit value`() {
        // given
        bankAccount.addTransaction(Credit("userLibelle", "BandLibelle", "", 10.0))
        bankAccount.addTransaction(Credit("userLibelle", "BandLibelle", "", 10.0))
        bankAccount.addTransaction(Debit("userLibelle", "BandLibelle", "", 5.0))


        // when
        bankAccount.soldeRefresh()

        // then
        assertThat(bankAccount.solde).isEqualTo(15.0)
    }

}

