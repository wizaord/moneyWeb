package com.wizaord.moneyweb.infrastructure.maria.domain

import com.wizaord.moneyweb.domain.User
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(
        @Id var id: String? = null,
        var username: String? = null,
        var password: String? = null,
        var email: String? = null) {

    companion object {
        fun fromDomain(user: User): com.wizaord.moneyweb.infrastructure.maria.domain.User {
            return com.wizaord.moneyweb.infrastructure.maria.domain.User(user.id, user.username, user.password, user.email)
        }
    }
    fun toDomain(): User {
        return User(username!!, password!!, email!!, id!!)
    }
}

