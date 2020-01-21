package com.wizaord.moneyweb.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Transaction(
        val libelleBanque: String,
        val detailLibelleBanque: String,
        val libelleUser: String,
        val dateReceptionBanque: Date,
        val dateReceptionMoneyWeb: Date? = Date(),
        val isPointe: Boolean,
        val fromAccountId: String?,
        val toAccountId: String?,
        val ventilations: MutableList<Ventilation> = mutableListOf(),
        @Id var id: String? = UUID.randomUUID().toString()
)



data class Ventilation(
        val montant: Double,
        val categorieId: String
)