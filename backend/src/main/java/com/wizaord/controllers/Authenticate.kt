package com.wizaord.controllers

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class Authenticate {

    @POST
    @Path("/authenticate")
    fun authenticate(user: UserConnect): JwtToken  {
        return JwtToken("")
    }

}

data class UserConnect(
        var username: String = "",
        var password: String = "")

data class JwtToken(
        var token: String)