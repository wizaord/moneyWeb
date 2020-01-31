package com.wizaord.moneyweb.truedomain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CategoryFamilyTest {

    lateinit var categoryFamily: CategoryFamily

    @BeforeEach
    internal fun init() {
        categoryFamily = CategoryFamily("category")
    }

    @Test
    internal fun `constructor - by default, a categoryFamily has an Id`() {
        // given

        // when

        // then
        assertThat(categoryFamily.id).isNotNull()
        assertThat(categoryFamily.name).isEqualTo("category")
    }

    @Test
    internal fun `addCategories - when a add a subcategory, thus the subcategory can be retrieve`() {
        // given
        val subCategory = SubCategory("subCate")

        // when
        categoryFamily.addSubCategory(subCategory)

        // then
        assertThat(categoryFamily.getSubCategories()).hasSize(1)
        assertThat(categoryFamily.getSubCategories()[0].name).isEqualTo(subCategory.name)
    }
}