package com.wizaord.moneyweb.infrastructure

import com.wizaord.moneyweb.domain.categories.Category
import com.wizaord.moneyweb.domain.categories.CategoryFamily
import com.wizaord.moneyweb.infrastructure.domain.CategoryFamilyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CategoryFamilyPersistenceImpl(
        @Autowired val categoryFamilyRepository: CategoryFamilyRepository
): CategoryFamilyPersistence {

    override fun save(categoryFamily: CategoryFamily) {
        categoryFamilyRepository.save(com.wizaord.moneyweb.infrastructure.domain.CategoryFamily(categoryFamily))
    }

    override fun getAll(): List<CategoryFamily> {
        return categoryFamilyRepository.findAll().map { it.toDomain() }
    }

    override fun getById(categoryId: String): Category {
        return getAll().mapNotNull { categoryFamily -> categoryFamily.findById(categoryId) }
                .first()
    }

}
