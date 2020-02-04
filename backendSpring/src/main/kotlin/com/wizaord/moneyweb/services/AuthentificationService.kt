package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.infrastructure.User
import com.wizaord.moneyweb.infrastructure.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthentificationService(
        private val userRepository: UserRepository) {

    fun createUser(login: String, password: String, email: String): User? {
        val findByUsername = userRepository.findByUsername(login)
        if (findByUsername != null) return null;
        val findByEmail = userRepository.findByEmail(email)
        if (findByEmail != null) return null

        val user = User(null, login, password, email)
        return userRepository.save(user)
    }

    fun getUserByUsernameAndPassword(username: String, password: String): User? {
        val findByUsername = userRepository.findByUsername(username)
        if (findByUsername?.password == password) {
            return findByUsername
        }
        return null
    }
}