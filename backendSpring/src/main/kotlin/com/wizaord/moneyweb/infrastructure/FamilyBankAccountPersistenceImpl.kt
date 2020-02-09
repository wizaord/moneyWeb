package com.wizaord.moneyweb.infrastructure

import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import com.wizaord.moneyweb.infrastructure.domain.FamilyBankAccount
import com.wizaord.moneyweb.infrastructure.domain.FamilyBankAccountsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
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
        return familyBankAccountsRepository.save(FamilyBankAccount.fromDomain(familyBankAccountsImpl)).toDomain()
    }

    override fun updateFamily(familyBankAccountsImpl: FamilyBankAccountsImpl): FamilyBankAccountsImpl {
        val familyToUpdate = FamilyBankAccount.fromDomain(familyBankAccountsImpl)
        return familyBankAccountsRepository.save(familyToUpdate).toDomain()
    }

}