package com.wizaord.moneyweb.domain.transactions

import java.time.LocalDate
import java.util.*
import kotlin.math.absoluteValue

class Debit(
        libelle: String,
        libelleBanque: String,
        descriptionBanque: String?,
        amount: Double,
        isPointe: Boolean = false,
        id: String = UUID.randomUUID().toString(),
        dateCreation: LocalDate = LocalDate.now()) : Transaction(amount.absoluteValue * -1, libelle, libelleBanque, descriptionBanque, isPointe, dateCreation, id) {

    override fun reverseTransaction(): Transaction {
        val credit = Credit(this.userLibelle, this.bankLibelle, this.bankDetail,
                this.amount * -1, this.isPointe, dateCreation = this.dateCreation)
        this.ventilations.map { it.reverseVentilation() }
                .forEach { credit.addVentilation(it) }
        return credit
    }
}

