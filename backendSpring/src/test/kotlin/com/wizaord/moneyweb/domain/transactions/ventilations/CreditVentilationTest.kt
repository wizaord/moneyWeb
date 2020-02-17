package com.wizaord.moneyweb.domain.transactions.ventilations

import com.wizaord.moneyweb.domain.categories.Category
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CreditVentilationTest {


    @Test
    internal fun `constructor ventilation - by default no category is specified`() {
        // when
        val ventilation = CreditVentilation(10.0)

        // then
        assertThat(ventilation.category).isNull()

    }

    @Test
    internal fun `setCategory - I can change the category of a ventilation`() {
        // given
        val ventilation = CreditVentilation(10.0)
        val category = Category("plop")

        // when
        ventilation.category = category

        // then
        assertThat(ventilation.category).isEqualTo(category)
    }
}