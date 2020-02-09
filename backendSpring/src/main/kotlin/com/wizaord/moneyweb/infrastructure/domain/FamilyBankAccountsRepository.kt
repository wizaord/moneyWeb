package com.wizaord.moneyweb.infrastructure.domain

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FamilyBankAccountsRepository : MongoRepository<FamilyBankAccount, String> {

}