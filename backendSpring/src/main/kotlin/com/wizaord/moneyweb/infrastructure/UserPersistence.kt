package com.wizaord.moneyweb.infrastructure

import com.wizaord.moneyweb.domain.User

interface UserPersistence {
    abstract fun findByUsername(login: String): User?
    abstract fun findByEmail(email: String): User?
    abstract fun save(user: User): User
}