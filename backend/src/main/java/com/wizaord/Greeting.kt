package com.wizaord

import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@ApplicationScoped
class GreetingService {

    fun greeting(name: String): GreetingBean {
        return GreetingBean("hello plopplop $name")
    }
}

@Path("/greeting")
class Greeting {

    @Inject
    lateinit var service: GreetingService

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun hello() = service.greeting("Hello kotlin")
}


data class GreetingBean(val message: String)
