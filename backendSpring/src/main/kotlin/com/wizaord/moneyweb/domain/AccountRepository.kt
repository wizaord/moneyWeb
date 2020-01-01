package com.wizaord.moneyweb.domain

import org.springframework.data.mongodb.repository.MongoRepository

interface AccountRepository : MongoRepository<Account, String> {

}