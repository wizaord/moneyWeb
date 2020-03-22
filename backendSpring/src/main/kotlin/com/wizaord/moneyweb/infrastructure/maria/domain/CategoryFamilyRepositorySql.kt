package com.wizaord.moneyweb.infrastructure.maria.domain

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.AttributeConverter

@Repository
interface CategoryFamilyRepositorySql : JpaRepository <Categories, String> {

}

class CategoryFamilyConverterJson : AttributeConverter<CategoryFamily, String> {

    private val objectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(p0: CategoryFamily): String {
        return objectMapper.writeValueAsString(p0);
    }

    override fun convertToEntityAttribute(p0: String): CategoryFamily {
        return objectMapper.readValue(p0, CategoryFamily::class.java)
    }
}