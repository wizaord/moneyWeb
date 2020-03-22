package com.wizaord.moneyweb.domain

import java.util.*

data class User(
        val username: String,
        val password: String,
        val email: String,
        val id: String = UUID.randomUUID().toString()) {

}
