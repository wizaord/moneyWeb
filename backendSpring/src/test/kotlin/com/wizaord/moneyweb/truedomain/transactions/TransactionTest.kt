package com.wizaord.moneyweb.truedomain.transactions

import com.wizaord.moneyweb.truedomain.transactions.ventilations.CreditVentilation
import com.wizaord.moneyweb.truedomain.transactions.ventilations.DebitVentilation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TransactionTest {

    @Test
    internal fun `contructor - By default a Transaction is not pointed`() {
        // given

        // when
        val transaction = Credit(10.0)

        // then
        assertThat(transaction.isPointe).isFalse()
    }


    @Test
    internal fun `mark - when I point a transaction, the transaction is pointed`() {
        // give
        val transaction = Credit(10.0)

        // when
        transaction.point()

        // then
        assertThat(transaction.isPointe).isTrue()

    }

    @Test
    internal fun `unmark - when I unpoint a transaction, the transaction is unpointed`() {
        // give
        val transaction = Credit(10.0)

        // when
        transaction.unpoint()

        // then
        assertThat(transaction.isPointe).isFalse()

    }

    @Test
    internal fun `ìsValid - A debit transaction is valided if the sum of the ventilations is equal to the transaction amount`() {
        // given
        val transaction = Debit(12.3)
        transaction.addVentilation(DebitVentilation(10.0))
        transaction.addVentilation(DebitVentilation(2.3))

        // when
        val isValid = transaction.isValid()

        // then
        assertThat(isValid).isTrue()
    }

    @Test
    internal fun `ìsValid - A debit transaction is not valid if the sum of the ventilations is not equal to the transaction amount`() {
        // given
        val transaction = Debit(12.3)
        transaction.addVentilation(DebitVentilation(10.0))
        transaction.addVentilation(DebitVentilation(2.2))

        // when
        val isValid = transaction.isValid()

        // then
        assertThat(isValid).isFalse()
    }

    @Test
    internal fun `ìsValid - A credit transaction is valided if the sum of the ventilations is equal to the transaction amount`() {
        // given
        val transaction = Credit(12.3)
        transaction.addVentilation(CreditVentilation(10.0))
        transaction.addVentilation(CreditVentilation(2.3))

        // when
        val isValid = transaction.isValid()

        // then
        assertThat(isValid).isTrue()
    }

    @Test
    internal fun `getVentilationById`() {
        // given
        val transaction = Credit(12.3)
        val ventilation = CreditVentilation(12.3)
        transaction.addVentilation(ventilation)

        // when
        val ventilationById = transaction.getVentilationById(ventilation.id)

        // then
        assertThat(ventilationById).isEqualTo(ventilation)
    }

    @Test
    internal fun `removeVentilation - when I remove a ventilation, thus ventilation is removed`() {
        // given
        val transaction = Credit(12.3)
        val ventilation = CreditVentilation(12.3)
        transaction.addVentilation(ventilation)

        // when
        transaction.removeVentilation(ventilation)

        // then
        assertThat(transaction.getVentilations()).hasSize(0)

    }
}