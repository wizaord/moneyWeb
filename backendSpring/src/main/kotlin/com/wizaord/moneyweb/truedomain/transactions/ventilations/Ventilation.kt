package com.wizaord.moneyweb.truedomain.transactions.ventilations

import com.wizaord.moneyweb.truedomain.categories.Category
import java.util.*


abstract class Ventilation(
        var amount: Double,
        var category: Category? = null,
        var id: String = UUID.randomUUID().toString()
)
