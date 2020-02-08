package com.wizaord.moneyweb.domain.transactions.ventilations

import com.wizaord.moneyweb.domain.categories.Category
import java.util.*


abstract class Ventilation(
        val amount: Double,
        private var category: Category? = null,
        val id: String = UUID.randomUUID().toString()
) {

    fun getCategory(): Category? = this.category
    fun setCategory(category: Category) {
        this.category = category
    }
}