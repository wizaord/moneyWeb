package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.User
import com.wizaord.moneyweb.domain.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    fun getUserByUsernameAndPassword(username: String, password: String): User? {
        val findByUsername = userRepository.findByUsername(username)
        if (findByUsername?.password == password) {
            return findByUsername
        }
        return null
    }

    fun createUser(login: String, password: String, email: String): Boolean {
        val findByUsername = userRepository.findByUsername(login)
        if (findByUsername != null) return false;

        val user = User(null, login, password, email)
        userRepository.insert(user)
        return true
    }

}
