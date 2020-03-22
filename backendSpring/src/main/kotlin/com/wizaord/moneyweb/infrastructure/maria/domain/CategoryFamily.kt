package com.wizaord.moneyweb.infrastructure.maria.domain

import com.wizaord.moneyweb.domain.categories.CategoryFamily
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "Categories")
class Categories(
        @Id
        var categoryId: String? = null,
        @Convert(converter = CategoryFamilyConverterJson::class)
        @Column(columnDefinition = "json")
        var family: com.wizaord.moneyweb.infrastructure.maria.domain.CategoryFamily? = null) {

}

class CategoryFamily(
        var id: String? = null,
        var name: String? = null,
        var categories: MutableList<Category> = mutableListOf()
) {

    companion object {
        fun fromDomain(categoryFamily: CategoryFamily): com.wizaord.moneyweb.infrastructure.maria.domain.CategoryFamily {
            return com.wizaord.moneyweb.infrastructure.maria.domain.CategoryFamily(categoryFamily.id,
                    categoryFamily.name,
                    categoryFamily.getSubCategories().map { Category(it) }.toMutableList())
        }
    }

    fun toDomain(): CategoryFamily {
        val categoryFamily = CategoryFamily(name!!, id!!)
        categories.forEach { categoryFamily.addSubCategory(it.toDomain()) }
        return categoryFamily
    }
}

class Category(
        var id: String? = null,
        var name: String? = null
) {

    fun toDomain() = com.wizaord.moneyweb.domain.categories.Category(name!!, id!!)
    constructor(category: com.wizaord.moneyweb.domain.categories.Category): this(category.id, category.name)
}
