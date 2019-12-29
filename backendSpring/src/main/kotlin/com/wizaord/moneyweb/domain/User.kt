package com.wizaord.moneyweb.domain

import org.springframework.data.annotation.Id

class User (
        @Id var id: String?,
        var username: String,
        var password: String,
        var email: String,
        var role: String = "USER")
