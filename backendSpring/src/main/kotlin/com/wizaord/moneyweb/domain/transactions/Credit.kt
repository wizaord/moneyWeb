package com.wizaord.moneyweb.domain.transactions

import java.util.*
import kotlin.math.absoluteValue

class Credit(
        libelle: String,
        libelleBanque: String,
        descriptionBanque: String?,
        amount: Double,
        isPointe: Boolean = false,
        id: String = UUID.randomUUID().toString()) : Transaction(amount.absoluteValue, libelle, libelleBanque, descriptionBanque, isPointe, id = id)
