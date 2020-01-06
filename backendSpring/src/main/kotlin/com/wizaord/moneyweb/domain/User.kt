package com.wizaord.moneyweb.domain

import org.springframework.data.annotation.Id

data class User(
        @Id var id: String?,
        var username: String,
        var password: String,
        var email: String) {
    var owners = mutableSetOf<AccountOwner>()

    fun addOwner(ownerName: String): Boolean {
        return this.owners.add(AccountOwner(ownerName)) //To change body of created functions use File | Settings | File Templates.
    }

    fun getOwner(owner: String): AccountOwner {
        return this.owners.first { it.name == owner }
    }
}

data class AccountOwner(
        var name: String
) {
    var accounts = mutableSetOf<String>()
}