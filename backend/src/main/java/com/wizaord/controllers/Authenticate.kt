package com.wizaord.controllers

import com.wizaord.services.JwtGeneratorService
import org.slf4j.LoggerFactory
import javax.annotation.security.PermitAll
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/moneyapi")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class Authenticate {

    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)

    @Inject
    lateinit var jwtService: JwtGeneratorService;

    @POST
    @PermitAll
    @Path("/authenticate")
    fun authenticate(user: UserConnect): JwtToken  {
        logger.info("Reception d'une demande d'auth pour l'utilisateur ${user.username}")
        return JwtToken(jwtService.generateToken(user.username, listOf("admin")))
    }

}

data class UserConnect(
        var username: String = "",
        var password: String = "")

data class JwtToken(
        var token: String)