package com.wizaord.services

import com.wizaord.domain.User
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserService {

    fun getUser(username: String, password: String): User? {
        return User.find<User>("username = ?1 and password = ?2", username, password).firstResult()
    }

}
