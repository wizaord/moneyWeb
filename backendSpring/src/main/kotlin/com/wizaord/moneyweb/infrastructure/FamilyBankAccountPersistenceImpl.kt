package com.wizaord.moneyweb.infrastructure

import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import com.wizaord.moneyweb.infrastructure.domain.FamilyBankAccount
import com.wizaord.moneyweb.infrastructure.domain.FamilyBankAccountsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FamilyBankAccountPersistenceImpl(
        @Autowired val familyBankAccountsRepository: FamilyBankAccountsRepository
) : FamilyBankAccountPersistence {

    override fun loadFamilyBankAccountByFamilyName(familyName: String): FamilyBankAccountsImpl? {
        val familyFromRepository = familyBankAccountsRepository.findById(familyName).map { it -> it.toDomain() }
        return when {
            familyFromRepository.isPresent -> familyFromRepository.get()
            else -> null
        }
    }

    override fun initFamily(familyBankAccountsImpl: FamilyBankAccountsImpl): FamilyBankAccountsImpl {
        return familyBankAccountsRepository.save(FamilyBankAccount(familyBankAccountsImpl)).toDomain()
    }

}