package com.wizaord.moneyweb.truedomain

import java.util.*

open class Category(
        var name: String,
        var id: String? = UUID.randomUUID().toString()
)

class CategoryFamily(
        name: String,
        id: String? = UUID.randomUUID().toString()
) : Category(name, id) {

    private val categories: MutableList<Category> = mutableListOf()

    fun addSubCategory(category: Category) = this.categories.add(category)
    fun getSubCategories() = this.categories
}