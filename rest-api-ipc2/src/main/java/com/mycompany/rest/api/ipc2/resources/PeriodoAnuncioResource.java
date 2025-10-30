package com.mycompany.rest.api.ipc2.resources;

import dtos.anuncios.PeriodoAnuncioResponse;
import exceptions.EntityNotFoundException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import models.anuncios.PeriodoAnuncio;
import services.anuncios.PeriodosAnuncioService;

@Path("periodos-anuncio")
public class PeriodoAnuncioResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPeriodosAnuncio() {
        PeriodosAnuncioService periodosService = new PeriodosAnuncioService();
        List<PeriodoAnuncioResponse> periodos = periodosService.getAllPeriodoAnuncio()
                .stream()
                .map(PeriodoAnuncioResponse::new)
                .toList();

        return Response.ok(periodos).build();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPeriodoAnuncioById(@PathParam("id") int id) {
        PeriodosAnuncioService periodosService = new PeriodosAnuncioService();
        try {
            PeriodoAnuncio periodo = periodosService.getPeriodoAnuncioById(id);
            return Response.ok(new PeriodoAnuncioResponse(periodo)).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}