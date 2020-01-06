package com.wizaord.moneyweb.domain

data class AccountOwner(
        var name: String
) {
    var accounts = mutableSetOf<String>()

    fun addAccount(accountId: String) = this.accounts.add(accountId)
}