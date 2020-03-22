package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.User
import com.wizaord.moneyweb.infrastructure.UserPersistence
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthentificationService(
        private val userPersistence: UserPersistence) {

    fun createUser(login: String, password: String, email: String): User? {
        val findByUsername = userPersistence.findByUsername(login)
        if (findByUsername != null) return null;
        val findByEmail = userPersistence.findByEmail(email)
        if (findByEmail != null) return null

        val user = User(login, password, email)
        return userPersistence.save(user)
    }

    fun getUserByUsernameAndPassword(username: String, password: String): User? {
        val findByUsername = userPersistence.findByUsername(username)
        if (findByUsername?.password == password) {
            return findByUsername
        }
        return null
    }
}