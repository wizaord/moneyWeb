package com.wizaord.moneyweb.truedomain.transactions.ventilations

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class CreditVentilationTest {


    @Test
    internal fun `constructor ventilation - by default no category is specified`() {
        // when
        val ventilation = CreditVentilation(10.0)

        // then
        Assertions.assertThat(ventilation.category).isNull()

    }
}