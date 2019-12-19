package com.wizaord.moneyweb.services

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class JwtService {

    companion object {
        const val ISSUER = "moneyweb.com"
    }

    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)

    @Value("\${moneyweb.jwt.secretKeyBase64}")
    lateinit var secretKey: String

    fun generateToken(username: String, roles: List<String> = listOf("USER")): String {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(3000, ChronoUnit.MILLIS)))
                .setId(UUID.randomUUID().toString())
                .addClaims(generateCustomClaims(roles))
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact()
    }

    fun generateSecretKey() {
        val keyPair = Keys.secretKeyFor(SignatureAlgorithm.HS512)
        println(Encoders.BASE64.encode(keyPair.encoded))
    }

    private fun generateCustomClaims(roles: List<String>): Map<String, String> {
        val customClaims = mutableMapOf<String, String>()
        customClaims["ROLE"] = roles[0]
        return customClaims
    }
}


