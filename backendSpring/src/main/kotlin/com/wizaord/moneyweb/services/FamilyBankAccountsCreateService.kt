package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import com.wizaord.moneyweb.domain.FamilyMember
import com.wizaord.moneyweb.infrastructure.FamilyBankAccontsRepository
import com.wizaord.moneyweb.infrastructure.FamilyBankAccount
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FamilyBankAccountsCreateService(
        @Autowired val familyBankAccontsRepository: FamilyBankAccontsRepository
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun initFamily(familyName: String): FamilyBankAccountsImpl {
        logger.info("Create new family with name : {}", familyName)
        val familyBankAccountsImpl = FamilyBankAccountsImpl(familyName)
        familyBankAccountsImpl.registerFamilyMember(FamilyMember(familyName))

        val familyBankInfra = familyBankAccontsRepository.save(FamilyBankAccount(familyBankAccountsImpl))

        return familyBankInfra.toDomain()

    }

}