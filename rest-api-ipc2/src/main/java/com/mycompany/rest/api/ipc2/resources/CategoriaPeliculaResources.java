package com.mycompany.rest.api.ipc2.resources;

import dtos.movies.CategoriaPeliculaResponse;
import exceptions.EntityNotFoundException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import models.movies.CategoriaPelicula;
import services.Movies.CategoriaPeliculaService;


@Path("categoria-peliculas")
public class CategoriaPeliculaResources {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCategoriasPelicula() {
        CategoriaPeliculaService categoriaService = new CategoriaPeliculaService();
        List<CategoriaPeliculaResponse> categorias = categoriaService.getAllCategoriasPelicula()
                .stream()
                .map(CategoriaPeliculaResponse::new)
                .toList();

        return Response.ok(categorias).build();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoriaPeliculaById(@PathParam("id") int id) {
        CategoriaPeliculaService categoriaService = new CategoriaPeliculaService();
        try {
            CategoriaPelicula categoria = categoriaService.getCategoriaPeliculaById(id);
            return Response.ok(new CategoriaPeliculaResponse(categoria)).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}