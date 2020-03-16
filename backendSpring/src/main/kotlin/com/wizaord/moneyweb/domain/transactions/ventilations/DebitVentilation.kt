package com.wizaord.moneyweb.domain.transactions.ventilations

import kotlin.math.absoluteValue

class DebitVentilation(amount: Double) : Ventilation(amount.absoluteValue * -1) {
    constructor(amount: Double, categoryId: String?) : this(amount) {
        this.categoryId = categoryId
    }

    override fun reverseVentilation(): Ventilation {
        return CreditVentilation(this.amount * -1, this.categoryId)
    }
}