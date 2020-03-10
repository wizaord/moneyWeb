package com.wizaord.moneyweb.domain.categories

import java.util.*


data class CategoryFamily(
        override var name: String,
        override var id: String = UUID.randomUUID().toString()
) : Category(name, id) {

    companion object {
        val VIREMENT_INTERNE_ID = "1"
    }

    private val categories: MutableList<Category> = mutableListOf()

    fun addSubCategory(category: Category) = this.categories.add(category)
    fun getSubCategories() = this.categories

    fun findById(categoryId: String): Category? {
        if (id == categoryId) return this
        return this.categories.firstOrNull { it.id == categoryId }
    }

    fun findByName(categoryName: String): Category? {
        if (name == categoryName) return this
        return this.categories.firstOrNull { it.name == categoryName }
    }
}