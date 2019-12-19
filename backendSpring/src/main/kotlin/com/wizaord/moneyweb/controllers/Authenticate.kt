package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.services.JwtService
import com.wizaord.moneyweb.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/moneyapi")
class Authenticate (
        @Autowired var userService: UserService,
        @Autowired var jwtService: JwtService){

    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)

    @PostMapping("/authenticate")
    @ResponseBody
    fun authenticate(@RequestBody user: UserConnect): ResponseEntity<JwtToken>  {
        logger.info("Reception d'une demande d'auth pour l'utilisateur ${user.username}")
        val userFromDb = userService.getUserByUsernameAndPassword(user.username, user.password)
                ?: return ResponseEntity(HttpStatus.FORBIDDEN)

        return ResponseEntity.ok(JwtToken(jwtService.generateToken(userFromDb.username, listOf(userFromDb.role))))
    }
}

data class UserConnect(
        var username: String = "",
        var password: String = "")

data class JwtToken(
        var token: String)