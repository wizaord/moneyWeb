package com.wizaord.moneyweb.infrastructure

import com.wizaord.moneyweb.domain.categories.CategoryFamily

interface CategoryFamilyPersistence {

    fun init(categoryFamily: CategoryFamily)
    fun getAll(): List<CategoryFamily>

}
