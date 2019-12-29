package com.wizaord.moneyweb.domain

import org.springframework.data.annotation.Id

data class User(
        @Id var id: String?,
        var username: String,
        var password: String,
        var email: String,
        var role: String = "USER") {
    var owners = mutableSetOf<AccountOwner>()

    fun addOwner(ownerName: String): Boolean {
        return this.owners.add(AccountOwner(ownerName)) //To change body of created functions use File | Settings | File Templates.
    }
}

data class AccountOwner(
        var name: String
)