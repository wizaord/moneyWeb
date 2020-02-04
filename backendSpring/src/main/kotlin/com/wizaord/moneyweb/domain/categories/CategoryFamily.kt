package com.wizaord.moneyweb.domain.categories

import java.util.*


class CategoryFamily(
        name: String,
        id: String? = UUID.randomUUID().toString()
) : Category(name, id) {

    private val categories: MutableList<Category> = mutableListOf()

    fun addSubCategory(category: Category) = this.categories.add(category)
    fun getSubCategories() = this.categories
}