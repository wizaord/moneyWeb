package com.wizaord.moneyweb.services

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.wizaord.moneyweb.domain.categories.CategoryFamily
import com.wizaord.moneyweb.infrastructure.CategoryFamilyPersistence
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
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
}