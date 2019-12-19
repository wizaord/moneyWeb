package com.wizaord.moneyweb.services

import org.springframework.stereotype.Service

@Service
class JwtService {

    fun generateToken(username: String, roles : List<String> ? = listOf()): String {
        return "";
    }
}
