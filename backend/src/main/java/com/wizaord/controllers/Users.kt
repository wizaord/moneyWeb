package com.wizaord.controllers

import org.eclipse.microprofile.jwt.JsonWebToken
import java.security.Principal
import javax.annotation.security.PermitAll
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/users")
@RequestScoped
class Users {

    @Inject
    lateinit var jwt: JsonWebToken

    @GET
    @Path("/")
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    fun users(@Context securityContext: SecurityContext): String {
        val caller: Principal = securityContext.userPrincipal
        val name = caller.name
        val hasClaims = this.jwt.claimNames.size != 0
        println(jwt.getClaim<String>("iss"))
        println(jwt.getClaim<String>("groups"))
        return "hello ${name}, isSecure: ${securityContext.isSecure}, authScheme: ${securityContext.authenticationScheme}, hasJWT: ${hasClaims}"
    }

}