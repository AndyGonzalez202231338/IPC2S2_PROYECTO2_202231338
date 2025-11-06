/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rest.api.ipc2.resources;

import db.ComentariosSalasDB;
import dtos.comentariosSalas.NewComentarioSalaRequest;

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
import models.comentario.ComentarioSala;
import services.comentariosSalas.ComentariosSalasCreator;


/**
 *
 * @author andy
 */
@Path("comentarios-sala")
public class ComentarioSalaResource {

    @Context
    UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createComentarioSala(NewComentarioSalaRequest NewComentarioSalaRequest) {
        ComentariosSalasCreator comentariosSalaCreator = new ComentariosSalasCreator();

        try {
            comentariosSalaCreator.createUser(NewComentarioSalaRequest);
            return Response.status(Response.Status.CREATED).build();
        } catch (UserDataInvalidException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                .entity("{\"error\": \"Ya has registrado un comentario para esta sala\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }
    }

    @GET
    @Path("/sala/{idSala}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComentariosBySala(@PathParam("idSala") int idSala) {
        ComentariosSalasDB comentariosSalasDB = new ComentariosSalasDB();
        List<ComentarioSala> comentarios = comentariosSalasDB.getComentariosBySala(idSala);
        return Response.ok(comentarios).build();
    }

    @GET
    @Path("/usuario/{idUsuario}/sala/{idSala}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComentariosByUsuarioAndSala(
            @PathParam("idUsuario") int idUsuario, 
            @PathParam("idSala") int idSala) {
        ComentariosSalasDB comentariosSalasDB = new ComentariosSalasDB();
        List<ComentarioSala> comentarios = comentariosSalasDB.getComentariosByUsuarioAndSala(idUsuario, idSala);
        return Response.ok(comentarios).build();
    }
}
