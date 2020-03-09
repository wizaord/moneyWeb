package com.wizaord.moneyweb.domain.transactions

import com.wizaord.moneyweb.domain.transactions.ventilations.CreditVentilation
import com.wizaord.moneyweb.domain.transactions.ventilations.DebitVentilation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TransactionTest {

    @Test
    internal fun `contructor - By default a Transaction is not pointed`() {
        // given

        // when
        val transaction = Credit("lib", "libBank", "desc", 10.0)

        // then
        assertThat(transaction.isPointe).isFalse()
    }


    @Test
    internal fun `mark - when I point a transaction, the transaction is pointed`() {
        // give
        val transaction = Credit("lib", "libBank", "desc", 10.0)

        // when
        transaction.point()

        // then
        assertThat(transaction.isPointe).isTrue()

    }

    @Test
    internal fun `unmark - when I unpoint a transaction, the transaction is unpointed`() {
        // give
        val transaction = Credit("lib", "libBank", "desc", 10.0)

        // when
        transaction.unpoint()

        // then
        assertThat(transaction.isPointe).isFalse()

    }

    @Test
    internal fun `ìsValid - A debit transaction is valided if the sum of the ventilations is equal to the transaction amount`() {
        // given
        val transaction = Debit("lib", "libBank", "desc", 12.3)
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
        val transaction = Debit("lib", "libBank", "desc", 12.3)
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
        val transaction = Credit("lib", "libBank", "desc", 12.3)
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
        val transaction = Credit("lib", "libBank", "desc", 12.3)
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
        val transaction = Credit("lib", "libBank", "desc", 12.3)
        val ventilation = CreditVentilation(12.3)
        transaction.addVentilation(ventilation)

        // when
        transaction.removeVentilation(ventilation)

        // then
        assertThat(transaction.ventilations).hasSize(0)

    }

    @Test
    internal fun `matchWith - transaction with the same banklibelle, return 0,9`() {
        // given
        val transaction = Credit("lib", "libBank", "desc", 12.3)
        val transactionMatch = Credit("lib2", "libBank", "desc2", 1.0)

        // when
        val matchPoint = transaction.matchWith(transactionMatch)

        // then
        assertThat(matchPoint).isEqualTo(0.9)
    }

    @Test
    internal fun `matchWith - transaction with the amount, return 0,3`() {
        // given
        val transaction = Credit("lib", "libBank", "desc", 12.3)
        val transactionMatch = Credit("lib2", "libBank2", "desc2", 12.3)

        // when
        val matchPoint = transaction.matchWith(transactionMatch)

        // then
        assertThat(matchPoint).isEqualTo(0.3)
    }

    @Test
    internal fun `matchWith - transaction with the userLibelle and bankLibelle, return 1,8`() {
        // given
        val transaction = Credit("lib", "libBank", "desc", 12.3)
        val transactionMatch = Credit("lib", "libBank", "desc2", 12.0)

        // when
        val matchPoint = transaction.matchWith(transactionMatch)

        // then
        assertThat(matchPoint).isEqualTo(1.9)
    }

    @Test
    internal fun `matchWith - transaction with the same bankDescription, return 0,8`() {
        // given
        val transaction = Credit("lib", "libBank", "desc", 12.3)
        val transactionMatch = Credit("lib2", "libBank2", "desc", 1.0)

        // when
        val matchPoint = transaction.matchWith(transactionMatch)

        // then
        assertThat(matchPoint).isEqualTo(0.8)
    }

    @Test
    internal fun `matchWith - transaction with the nothing equals, return 0,0`() {
        // given
        val transaction = Credit("lib", "libBank", "desc", 12.3)
        val transactionMatch = Credit("lib2", "libBank2", "desc2", 1.0)

        // when
        val matchPoint = transaction.matchWith(transactionMatch)

        // then
        assertThat(matchPoint).isEqualTo(0.0)
    }
}