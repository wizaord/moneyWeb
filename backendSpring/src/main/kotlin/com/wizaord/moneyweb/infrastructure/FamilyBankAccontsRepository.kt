package com.wizaord.moneyweb.infrastructure

import org.springframework.data.mongodb.repository.MongoRepository

interface FamilyBankAccontsRepository : MongoRepository<FamilyBankAccount, String> {

}