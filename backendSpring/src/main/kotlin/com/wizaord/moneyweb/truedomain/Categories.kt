package com.wizaord.moneyweb.truedomain

import java.util.*

data class SubCategory(
        var name: String,
        var id: String? = UUID.randomUUID().toString()
)

data class CategoryFamily(
        var name: String,
        var id: String? = UUID.randomUUID().toString()
) {

    private val subCategories: MutableList<SubCategory> = mutableListOf()

    fun addSubCategory(category: SubCategory) = this.subCategories.add(category)
    fun getSubCategories() = this.subCategories
}