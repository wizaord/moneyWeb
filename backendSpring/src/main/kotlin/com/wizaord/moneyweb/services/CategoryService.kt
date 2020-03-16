package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.categories.CategoryFamily
import com.wizaord.moneyweb.infrastructure.CategoryFamilyPersistence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CategoryService(
        @Autowired val categoryFamilyPersistence: CategoryFamilyPersistence
) {
    fun createCategory(categoryFamily: CategoryFamily) = this.categoryFamilyPersistence.init(categoryFamily)
    fun getAll(): List<CategoryFamily> = this.categoryFamilyPersistence.getAll()
    fun isVirementCategory(categoryId: String?): Boolean {
        if (categoryId == null) return false
        val categoryFamily = this.getAll().firstOrNull() { it.findById(categoryId) != null }
        return categoryFamily?.id == CategoryFamily.VIREMENT_INTERNE_ID
    }

    fun getAccountNameVirementDestination(categoryId: String): String {
        return this.getAll().map { it.findById(categoryId) }
                .filterNotNull()
                .map { it.name.substringAfter("-").trim() }
                .first()
    }
}