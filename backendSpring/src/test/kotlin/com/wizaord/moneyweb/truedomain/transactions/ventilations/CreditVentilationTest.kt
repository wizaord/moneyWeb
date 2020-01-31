package com.wizaord.moneyweb.truedomain.transactions.ventilations

import com.wizaord.moneyweb.truedomain.categories.Category
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CreditVentilationTest {


    @Test
    internal fun `constructor ventilation - by default no category is specified`() {
        // when
        val ventilation = CreditVentilation(10.0)

        // then
        assertThat(ventilation.getCategory()).isNull()

    }

    @Test
    internal fun `setCategory - I can change the category of a ventilation`() {
        // given
        val ventilation = CreditVentilation(10.0)
        val category = Category("plop")

        // when
        ventilation.setCategory(category)

        // then
        assertThat(ventilation.getCategory()).isEqualTo(category)
    }
}