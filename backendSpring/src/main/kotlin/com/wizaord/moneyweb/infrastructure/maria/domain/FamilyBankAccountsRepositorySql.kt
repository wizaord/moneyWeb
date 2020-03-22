package com.wizaord.moneyweb.infrastructure.maria.domain

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.AttributeConverter


@Repository
interface FamilyBankAccountsRepositorySql : JpaRepository<Family, String> {
}

class FamilyConverterJson : AttributeConverter<FamilyBankAccount, String> {

    private val objectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(p0: FamilyBankAccount): String {
        return objectMapper.writeValueAsString(p0);
    }

    override fun convertToEntityAttribute(p0: String): FamilyBankAccount {
        return objectMapper.readValue(p0, FamilyBankAccount::class.java)
    }
}