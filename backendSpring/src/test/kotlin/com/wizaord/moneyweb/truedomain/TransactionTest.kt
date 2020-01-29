package com.wizaord.moneyweb.truedomain

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
}