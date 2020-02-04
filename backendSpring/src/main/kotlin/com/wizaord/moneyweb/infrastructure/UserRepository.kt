package com.wizaord.moneyweb.infrastructure

import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {

    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?

}