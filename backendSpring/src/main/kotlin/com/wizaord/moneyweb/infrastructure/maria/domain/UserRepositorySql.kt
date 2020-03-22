package com.wizaord.moneyweb.infrastructure.maria.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepositorySql : JpaRepository<User, String> {

    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?

}