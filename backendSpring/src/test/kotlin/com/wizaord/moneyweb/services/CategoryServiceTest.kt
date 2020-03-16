package com.wizaord.moneyweb.services

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.wizaord.moneyweb.domain.categories.Category
import com.wizaord.moneyweb.domain.categories.CategoryFamily
import com.wizaord.moneyweb.infrastructure.CategoryFamilyPersistence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class CategoryServiceTest {

    @Mock
    lateinit var categoryFamilyPersistence: CategoryFamilyPersistence

    @InjectMocks
    lateinit var categoryService: CategoryService

    @Test
    internal fun `init - when function is called, function from persistence is called`() {
        // given
        val categoryFamily = CategoryFamily("name", "id")
        given(categoryFamilyPersistence.init(anyOrNull())).willAnswer { i -> i.arguments[0] }

        // when
        categoryService.createCategory(categoryFamily)

        // then
        verify(categoryFamilyPersistence).init(categoryFamily)
    }

    @Test
    internal fun `getAll - return all familyCategories`() {
        // given
        val categoryFamily = CategoryFamily("name", "id")
        val categoryFamily2 = CategoryFamily("name", "id")

        given(categoryFamilyPersistence.getAll()).willReturn(listOf(categoryFamily, categoryFamily2))

        // when
        val all = categoryService.getAll()

        // then
        assertThat(all).containsExactly(categoryFamily, categoryFamily2)
    }

    @Test
    internal fun `isVirementCategory - if category is a child of the VIREMENT family, then return true`() {
        // given
        val virementCategoryFamily = CategoryFamily("name", CategoryFamily.VIREMENT_INTERNE_ID)
        virementCategoryFamily.addSubCategory(Category("subCate", "2"))

        given(categoryFamilyPersistence.getAll()).willReturn(listOf(virementCategoryFamily))

        // when
        val virementCategory = categoryService.isVirementCategory("2")

        // then
        assertThat(virementCategory).isTrue()
    }

    @Test
    internal fun `isVirementCategory - if null return false`() {
        assertThat(categoryService.isVirementCategory(null)).isFalse()
    }

    @Test
    internal fun `getAccountNameVirementDestination - if VIREMENT - BO-LivreA then return BO-LibreA`() {
        // given
        val virementCategoryFamily = CategoryFamily("name", CategoryFamily.VIREMENT_INTERNE_ID)
        virementCategoryFamily.addSubCategory(Category("VIREMENT - BO-LivreA", "2"))
        given(categoryFamilyPersistence.getAll()).willReturn(listOf(virementCategoryFamily))

        // when
        val accountName = categoryService.getAccountNameVirementDestination("2")

        // then
        assertThat(accountName).isEqualTo("BO-LivreA")
    }

}