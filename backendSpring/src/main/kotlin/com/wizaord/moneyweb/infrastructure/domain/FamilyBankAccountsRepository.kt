package com.wizaord.moneyweb.infrastructure.domain

import com.wizaord.moneyweb.infrastructure.domain.FamilyBankAccount
import org.springframework.data.mongodb.repository.MongoRepository

interface FamilyBankAccountsRepository : MongoRepository<FamilyBankAccount, String> {

}