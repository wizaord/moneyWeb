package com.wizaord.moneyweb.infrastructure.mongo

import com.wizaord.moneyweb.domain.User
import com.wizaord.moneyweb.infrastructure.UserPersistence
import com.wizaord.moneyweb.infrastructure.mongo.domain.UserRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
@Profile("mongo")
class UserPersistenceImpl(
        private val userRepository: UserRepository
): UserPersistence {
    override fun findByUsername(login: String): User? {
        return this.userRepository.findByUsername(login)?.toDomain()
    }

    override fun findByEmail(email: String): User? {
        return this.userRepository.findByEmail(email)?.toDomain()
    }

    override fun save(user: User): User {
        return this.userRepository.save(com.wizaord.moneyweb.infrastructure.mongo.domain.User.fromDomain(user)).toDomain()
    }
}