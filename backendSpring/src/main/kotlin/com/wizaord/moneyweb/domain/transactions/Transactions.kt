package com.wizaord.moneyweb.domain.transactions

import com.wizaord.moneyweb.domain.transactions.ventilations.Ventilation
import java.time.LocalDate
import java.util.*
import kotlin.NoSuchElementException

abstract class Transaction(
        val amount: Double,
        var userLibelle: String,
        val bankLibelle: String,
        val bankDetail: String?,
        var isPointe: Boolean = false,
        var dateCreation: LocalDate = LocalDate.now(),
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
    fun getVentilationById(id: String) = this.ventilations.first { it.id == id }

    fun isValid(): Boolean {
        return amount == ventilations.sumByDouble { it.amount }
    }

    fun matchWith(transaction: Transaction): Double {
        var score = 0.0
        if (transaction.userLibelle == this.userLibelle) score += 1.0
        if (transaction.bankLibelle == this.bankLibelle) score += 0.9
        if (this.bankDetail != null && transaction.bankDetail == this.bankDetail) score += 0.8
        if (score > 0) {
            if (transaction.amount == this.amount) score += 0.3
        }
        return score
    }

}