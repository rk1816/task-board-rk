package com.taskboard.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/test")
public class TestResource {
    @GET
    public Response sayHello() {
        return Response.ok("I AM UP and RUNNING!").build();
    }
}
