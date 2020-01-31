package com.wizaord.moneyweb.truedomain.transactions

import com.wizaord.moneyweb.truedomain.transactions.ventilations.Ventilation
import java.util.*
import kotlin.NoSuchElementException

abstract class Transaction(
        val amount: Double,
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