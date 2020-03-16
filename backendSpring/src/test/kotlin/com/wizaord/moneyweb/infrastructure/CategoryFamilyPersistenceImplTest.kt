package com.wizaord.moneyweb.infrastructure

import com.nhaarman.mockitokotlin2.given
import com.wizaord.moneyweb.infrastructure.domain.Category
import com.wizaord.moneyweb.infrastructure.domain.CategoryFamily
import com.wizaord.moneyweb.infrastructure.domain.CategoryFamilyRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class CategoryFamilyPersistenceImplTest {

    @Mock
    lateinit var categoryFamilyRepository: CategoryFamilyRepository

    @InjectMocks
    lateinit var categoryFamilyPersistence: CategoryFamilyPersistenceImpl

    @Test
    internal fun `getAll - return all category from database`() {
        // given
        val category1 = CategoryFamily("category", "id")
        val category2 = CategoryFamily("category2", "id2")
        given(categoryFamilyRepository.findAll()).willReturn(mutableListOf(
                category1,
                category2
        ))

        // when
        val allCategories = categoryFamilyPersistence.getAll()

        // then
        val categoryDomain1 = com.wizaord.moneyweb.domain.categories.CategoryFamily("category", category1.id)
        val categoryDomain2 = com.wizaord.moneyweb.domain.categories.CategoryFamily("category2", category2.id)
        assertThat(allCategories).isNotNull.isNotEmpty.containsExactly(categoryDomain1, categoryDomain2)
    }

    @Test
    internal fun `getById - return only category with Id`() {
        // given
        val category1 = CategoryFamily("category", "id")
        val category2 = CategoryFamily("category2", "id2")
        given(categoryFamilyRepository.findAll()).willReturn(mutableListOf(
                category1,
                category2
        ))

        // when
        val categoryReturned = categoryFamilyPersistence.getById(category2.id)

        // then
        val categoryDomain2 = com.wizaord.moneyweb.domain.categories.CategoryFamily("category2", category2.id)
        assertThat(categoryReturned).isNotNull.isEqualTo(categoryDomain2)
    }

    @Test
    internal fun `getById - return only category with sub category with Id`() {
        // given
        val category1 = CategoryFamily("category", "id")
        category1.categories.add(Category("subCategory", "id2"))
        val category2 = CategoryFamily("category2", "id3")
        category2.categories.add(Category("SubCategory2", "id4"))
        given(categoryFamilyRepository.findAll()).willReturn(mutableListOf(
                category1,
                category2
        ))

        // when
        val categoryReturned = categoryFamilyPersistence.getById(category2.categories[0].id)

        // then
        assertThat(categoryReturned.name).isEqualTo("SubCategory2")
    }

}