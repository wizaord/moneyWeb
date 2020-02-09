package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.categories.CategoryFamily
import com.wizaord.moneyweb.infrastructure.CategoryFamilyPersistence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CategoryService(
        @Autowired var categoryFamilyPersistence: CategoryFamilyPersistence
) {
    fun createCategory(categoryFamily: CategoryFamily) = this.categoryFamilyPersistence.init(categoryFamily)
    fun getAll(): List<CategoryFamily> = this.categoryFamilyPersistence.getAll()
}