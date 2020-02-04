package com.wizaord.moneyweb.domain.transactions

import com.wizaord.moneyweb.domain.transactions.ventilations.Ventilation
import java.time.LocalDateTime
import java.util.*
import kotlin.NoSuchElementException

abstract class Transaction(
        val amount: Double,
        var userLibelle: String,
        val bankLibelle: String,
        val bankDetail: String?,
        var isPointe: Boolean = false,
        var dateCreation: LocalDateTime = LocalDateTime.now(),
        val id: String = UUID.randomUUID().toString()
) {

    private val ventilations = mutableListOf<Ventilation>()

    fun point() {
        this.isPointe = true
    }

    fun unpoint() {
        this.isPointe = false
    }

    fun addVentilation(ventilation: Ventilation) = this.ventilations.add(ventilation)
    fun removeVentilation(ventilation: Ventilation) = this.ventilations.remove(ventilation)
    fun getVentilations() = this.ventilations.toList()

    @Throws(NoSuchElementException::class)
    fun getVentilationById(id: String) = this.ventilations.first { it.id == id }

    fun isValid(): Boolean {
        return amount == ventilations.sumByDouble { it.amount }
    }

    fun matchWith(transaction: Transaction): Double {
        return when {
            transaction.userLibelle == this.userLibelle -> 1.0
            transaction.bankLibelle == this.bankLibelle -> 0.9
            transaction.bankDetail == this.bankDetail -> 0.8
            else -> 0.0
        }
    }

}