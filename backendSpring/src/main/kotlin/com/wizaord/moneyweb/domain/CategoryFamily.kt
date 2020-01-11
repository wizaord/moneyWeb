package com.wizaord.moneyweb.domain

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*


data class SubCategory(
        var name: String,
        var id: String? = UUID.randomUUID().toString()
)

@Document
data class CategoryFamily(
        var name: String,
        var subCategories: MutableSet<SubCategory> = mutableSetOf(),
        var id: String? = UUID.randomUUID().toString()
) {
    fun addSubCategory(category: SubCategory) {
        this.subCategories.add(category)
    }
}