package com.mycompany.rest.api.ipc2.resources;

import dtos.anuncios.TipoAnuncioResponse;
import exceptions.EntityNotFoundException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import models.anuncios.TipoAnuncio;
import services.anuncios.TiposAnuncioService;

@Path("tipos-anuncio")
public class TipoAnuncioResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTiposAnuncio() {
        TiposAnuncioService tiposService = new TiposAnuncioService();
        List<TipoAnuncioResponse> tipos = tiposService.getAllTiposAnuncio()
                .stream()
                .map(TipoAnuncioResponse::new)
                .toList();

        return Response.ok(tipos).build();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTipoAnuncioById(@PathParam("id") int id) {
        TiposAnuncioService tiposService = new TiposAnuncioService();
        try {
            TipoAnuncio tipo = tiposService.getTipoAnuncioById(id);
            return Response.ok(new TipoAnuncioResponse(tipo)).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}