package com.wizaord.moneyweb.infrastructure.maria

import com.wizaord.moneyweb.domain.categories.Category
import com.wizaord.moneyweb.domain.categories.CategoryFamily
import com.wizaord.moneyweb.infrastructure.CategoryFamilyPersistence
import com.wizaord.moneyweb.infrastructure.maria.domain.Categories
import com.wizaord.moneyweb.infrastructure.maria.domain.CategoryFamilyRepositorySql
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
@Profile("maria")
class CategoryFamilyPersistenceImpl(
        private val categoryFamilyRepositorySql: CategoryFamilyRepositorySql
) : CategoryFamilyPersistence {

    override fun save(categoryFamily: CategoryFamily) {
        categoryFamilyRepositorySql.save(Categories(categoryFamily.id,
                com.wizaord.moneyweb.infrastructure.maria.domain.CategoryFamily.fromDomain(categoryFamily)))
    }

    override fun getAll(): List<CategoryFamily> {
        return categoryFamilyRepositorySql.findAll()
                .map { it.family!!.toDomain() }
    }

    override fun getById(categoryId: String): Category {
        return getAll().mapNotNull { categoryFamily -> categoryFamily.findById(categoryId) }
                .first()
    }

    override fun deleteAll() {
        categoryFamilyRepositorySql.deleteAll()
    }
}