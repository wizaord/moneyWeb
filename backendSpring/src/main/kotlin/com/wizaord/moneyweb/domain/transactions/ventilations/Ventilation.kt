package com.wizaord.moneyweb.domain.transactions.ventilations

import java.util.*

abstract class Ventilation(
        val amount: Double,
        var categoryId: String? = null,
        val id: String = UUID.randomUUID().toString()
) {

    abstract fun reverseVentilation(): Ventilation
}
