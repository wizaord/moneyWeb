package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.User
import com.wizaord.moneyweb.domain.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
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

    fun createUser(login: String, password: String, email: String): User? {
        val findByUsername = userRepository.findByUsername(login)
        if (findByUsername != null) return null;
        val findByEmail = userRepository.findByEmail(email)
        if (findByEmail != null) return null

        val user = User(null, login, password, email)
        return userRepository.save(user)
    }

    fun addOwner(userId: String, ownerName: String): User {
        val user = userRepository.findById(userId).get()
        if (user.addOwner(ownerName)) return userRepository.save(user)
        return user;
    }

    fun getCurrentUser(): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
