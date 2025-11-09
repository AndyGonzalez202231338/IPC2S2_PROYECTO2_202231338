package com.mycompany.rest.api.ipc2.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("simple-test")
public class SimpleTestResource {
    
    @GET
    @Path("hola")
    @Produces(MediaType.TEXT_PLAIN)
    public String holaMundo() {
        return "¡HOLA MUNDO! El endpoint funciona correctamente";
    }
}