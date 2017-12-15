package com.vates.eng.ha.server.demo.service.web;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

@Service("server.service.demo")
@Path("/server")
public class TestWebService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/echo/{message}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response echo(final @PathParam("message") String message) {

        return Response.ok().entity(message).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/echo")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response echo() {

        return Response.ok().entity("ECHO IS WORKING!!!").build();

    }
    
}
