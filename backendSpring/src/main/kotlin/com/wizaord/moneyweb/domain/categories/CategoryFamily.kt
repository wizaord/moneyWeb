package com.wizaord.moneyweb.domain.categories

import java.util.*


data class CategoryFamily(
        override var name: String,
        override var id: String = UUID.randomUUID().toString()
) : Category(name, id) {

    private val categories: MutableList<Category> = mutableListOf()

    fun addSubCategory(category: Category) = this.categories.add(category)
    fun getSubCategories() = this.categories

    fun findById(categoryId: String): Category? {
        if (id == categoryId) return this
        return this.categories.firstOrNull { it.id == categoryId }
    }
}