package com.wizaord.controllers

import com.wizaord.services.JwtGeneratorService
import javax.annotation.security.PermitAll
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class Authenticate {

    @Inject
    lateinit var jwtService: JwtGeneratorService;

    @POST
    @PermitAll
    @Path("/authenticate")
    fun authenticate(user: UserConnect): JwtToken  {
        return JwtToken(jwtService.generateToken(user.username))
    }

}

data class UserConnect(
        var username: String = "",
        var password: String = "")

data class JwtToken(
        var token: String)