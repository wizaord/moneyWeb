package com.wizaord.moneyweb.truedomain

import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.absoluteValue

abstract class Transaction(
        var amount: Double,
        var isPointe: Boolean = false,
        val id: String = UUID.randomUUID().toString()
) {

    val ventilations = mutableListOf<Ventilation>()

    fun point() {
        this.isPointe = true
    }
    fun unpoint() {
        this.isPointe = false
    }

    fun addVentilation(ventilation: Ventilation) = this.ventilations.add(ventilation)
    fun removeVentilation(ventilation: Ventilation) = this.ventilations.remove(ventilation)

    @Throws(NoSuchElementException::class)
    fun getVentilationById(id: String) = this.ventilations.first { it.id == id}

    fun isValid(): Boolean {
        return amount == ventilations.sumByDouble { it.amount }
    }

}

abstract class Ventilation(
        var amount: Double,
        var id: String = UUID.randomUUID().toString()
)

class DebitVentilation(amount: Double): Ventilation(amount.absoluteValue * -1)
class CreditVentilation(amount: Double): Ventilation(amount)

class Debit(amount: Double) : Transaction(amount.absoluteValue * -1)
class Credit(amount: Double) : Transaction(amount.absoluteValue)
