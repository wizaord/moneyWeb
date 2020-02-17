package com.wizaord.moneyweb.infrastructure.domain

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionsRepository : MongoRepository<Transaction, String> {

    fun findByAccountInternalId(accountInternalId: String): List<Transaction>

}