package com.wizaord.moneyweb.infrastructure

import com.wizaord.moneyweb.domain.categories.Category
import com.wizaord.moneyweb.domain.categories.CategoryFamily

interface CategoryFamilyPersistence {

    fun save(categoryFamily: CategoryFamily)
    fun getAll(): List<CategoryFamily>
    fun getById(categoryId: String): Category
    fun deleteAll()
}
