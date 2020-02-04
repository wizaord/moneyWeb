package com.wizaord.moneyweb.infrastructure

import org.springframework.data.annotation.Id

data class User(
        @Id var id: String?,
        var username: String,
        var password: String,
        var email: String) {
}

