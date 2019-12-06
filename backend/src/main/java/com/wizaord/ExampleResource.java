package com.wizaord;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class ExampleResource {

    @Inject
    GreetingService greetingService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GreetingBean hello() {
        return greetingService.greeting("okijuh");
    }
}