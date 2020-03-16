package com.wizaord.moneyweb.domain.transactions.ventilations

class CreditVentilation(amount: Double): Ventilation(amount) {
    constructor(amount: Double, categoryId: String?) : this(amount) {
        this.categoryId = categoryId
    }

    override fun reverseVentilation(): Ventilation {
        return DebitVentilation(this.amount, this.categoryId)
    }
}
