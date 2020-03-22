package com.wizaord.moneyweb.infrastructure.maria

import com.wizaord.moneyweb.domain.User
import com.wizaord.moneyweb.infrastructure.UserPersistence
import com.wizaord.moneyweb.infrastructure.maria.domain.UserRepositorySql
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
@Profile("maria")
class UserPersistenceImpl(
        private val userRepository: UserRepositorySql
): UserPersistence {
    @Transactional(readOnly = true)
    override fun findByUsername(login: String): User? {
        return this.userRepository.findByUsername(login)?.toDomain()
    }

    @Transactional(readOnly = true)
    override fun findByEmail(email: String): User? {
        return this.userRepository.findByEmail(email)?.toDomain()
    }

    override fun save(user: User): User {
        return this.userRepository.save(com.wizaord.moneyweb.infrastructure.maria.domain.User.fromDomain(user)).toDomain()
    }
}