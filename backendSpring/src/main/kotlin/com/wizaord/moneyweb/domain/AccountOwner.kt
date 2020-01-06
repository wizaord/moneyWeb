package com.wizaord.moneyweb.domain

data class AccountOwner(
        var name: String
) {
    var accounts = mutableSetOf<String>()
}