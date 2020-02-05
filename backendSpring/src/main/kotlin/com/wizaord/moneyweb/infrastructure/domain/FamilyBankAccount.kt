package com.wizaord.moneyweb.infrastructure.domain

import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class FamilyBankAccount(
        @Id val familyId: String,
        val familyMembers: List<FamilyMember>
) {
    fun toDomain(): FamilyBankAccountsImpl {
        val familyBankAccountsImpl = FamilyBankAccountsImpl(familyId)
        familyMembers.forEach { familyBankAccountsImpl.registerFamilyMember(it.toDomain())}
        return familyBankAccountsImpl
    }

    constructor(f: FamilyBankAccountsImpl) :
            this(f.familyName,
                    f.getFamily().map { FamilyMember(it.username) }) {
    }

}


data class FamilyMember(val username: String) {
    fun toDomain(): com.wizaord.moneyweb.domain.FamilyMember {
        return com.wizaord.moneyweb.domain.FamilyMember(username)
    }
}