package com.wizaord.moneyweb.domain.transactions

import java.time.LocalDate
import java.util.*
import kotlin.math.absoluteValue

class Credit(
        libelle: String,
        libelleBanque: String,
        descriptionBanque: String?,
        amount: Double,
        isPointe: Boolean = false,
        id: String = UUID.randomUUID().toString(),
        dateCreation: LocalDate = LocalDate.now()) : Transaction(amount.absoluteValue, libelle, libelleBanque, descriptionBanque, isPointe, dateCreation, id)
