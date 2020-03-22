package com.wizaord.moneyweb.infrastructure.mongo.domain

import com.wizaord.moneyweb.domain.User
import org.springframework.data.annotation.Id

data class User(
        @Id var id: String,
        var username: String,
        var password: String,
        var email: String) {

    companion object {
        fun fromDomain(user: User): com.wizaord.moneyweb.infrastructure.mongo.domain.User {
            return com.wizaord.moneyweb.infrastructure.mongo.domain.User(user.id, user.username, user.password, user.email)
        }
    }
    fun toDomain(): User {
        return User(username, password, email, id)
    }
}

