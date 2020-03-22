package com.wizaord.moneyweb.infrastructure.maria.domain

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.AttributeConverter

@Repository
interface TransactionsRepositorySql : JpaRepository <Transaction, String> {
    fun findByAccountInternalId(accountInternalId: String): List<Transaction>
    fun findByAccountInternalIdIn(accountInternalIds: List<String>): List<Transaction>
}

class VentilationConverterJson : AttributeConverter<Ventilations, String> {

    private val objectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(p0: Ventilations): String {
        return objectMapper.writeValueAsString(p0);
    }

    override fun convertToEntityAttribute(p0: String?): Ventilations? {
        if (p0 == null) return null
        return objectMapper.readValue(p0, Ventilations::class.java)
    }
}