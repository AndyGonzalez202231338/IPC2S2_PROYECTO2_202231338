package com.mycompany.rest.api.ipc2.resources;

import dtos.comentariosPeliculas.NewComentarioPeliculaRequest;
import exceptions.EntityAlreadyExistsException;
import exceptions.UserDataInvalidException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.List;
import models.comentario.ComentarioPelicula;
import services.comentariosPeliculas.ComentariosPeliculasCreator;

@Path("comentarios-pelicula")
public class ComentarioPeliculaResource {

    @Context
    UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createComentarioPelicula(NewComentarioPeliculaRequest NewComentarioPeliculaRequest) {
        ComentariosPeliculasCreator comentariosPeliculaCreator = new ComentariosPeliculasCreator();

        try {
            comentariosPeliculaCreator.createComentarioPelicula(NewComentarioPeliculaRequest);
            return Response.status(Response.Status.CREATED).build();
        } catch (UserDataInvalidException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Path("/pelicula/{idPelicula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComentariosByPelicula(@PathParam("idPelicula") int idPelicula) {
        db.ComentariosPeliculasDB comentariosPeliculasDB = new db.ComentariosPeliculasDB();
        List<ComentarioPelicula> comentarios = comentariosPeliculasDB.getComentariosByPelicula(idPelicula);
        return Response.ok(comentarios).build();
    }

    @GET
    @Path("/usuario/{idUsuario}/pelicula/{idPelicula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComentariosByUsuarioAndPelicula(
            @PathParam("idUsuario") int idUsuario, 
            @PathParam("idPelicula") int idPelicula) {
        db.ComentariosPeliculasDB comentariosPeliculasDB = new db.ComentariosPeliculasDB();
        List<ComentarioPelicula> comentarios = comentariosPeliculasDB.getComentariosByUsuarioAndPelicula(idUsuario, idPelicula);
        return Response.ok(comentarios).build();
    }
}