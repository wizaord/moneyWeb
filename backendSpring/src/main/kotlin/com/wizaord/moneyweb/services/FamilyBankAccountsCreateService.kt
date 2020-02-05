package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import com.wizaord.moneyweb.domain.FamilyMember
import com.wizaord.moneyweb.infrastructure.FamilyBankAccountPersistence
import com.wizaord.moneyweb.infrastructure.domain.FamilyBankAccountsRepository
import com.wizaord.moneyweb.infrastructure.domain.FamilyBankAccount
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FamilyBankAccountsCreateService(
        @Autowired val familyBankAccountPersistence: FamilyBankAccountPersistence
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun initFamily(familyName: String): FamilyBankAccountsImpl {
        logger.info("Create new family with name : {}", familyName)
        val familyBankAccountsImpl = FamilyBankAccountsImpl(familyName)
        familyBankAccountsImpl.registerFamilyMember(FamilyMember(familyName))

        return familyBankAccountPersistence.initFamily(familyBankAccountsImpl)
    }

}