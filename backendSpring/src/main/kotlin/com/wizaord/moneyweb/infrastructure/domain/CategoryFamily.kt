package com.wizaord.moneyweb.infrastructure.domain

import com.wizaord.moneyweb.domain.categories.CategoryFamily
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
class CategoryFamily(
        name: String,
        id: String = UUID.randomUUID().toString(),
        val categories: MutableList<Category> = mutableListOf()
) : Category(name, id) {

    constructor(categoryFamily: CategoryFamily):
            this(categoryFamily.name,
                    categoryFamily.id,
                    categoryFamily.getSubCategories().map { Category(it) }.toMutableList())


    override fun toDomain(): CategoryFamily {
        val categoryFamily = com.wizaord.moneyweb.domain.categories.CategoryFamily(name, id)
        categories.forEach { categoryFamily.addSubCategory(it.toDomain()) }
        return categoryFamily
    }
}


open class Category(
        var name: String,
        var id: String = UUID.randomUUID().toString()
) {

    open fun toDomain() = com.wizaord.moneyweb.domain.categories.Category(name, id)

    constructor(category: com.wizaord.moneyweb.domain.categories.Category): this(category.name, category.id)
}
