package com.wizaord.moneyweb.infrastructure.mongo.domain

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FamilyBankAccountsRepository : MongoRepository<FamilyBankAccount, String> {

}