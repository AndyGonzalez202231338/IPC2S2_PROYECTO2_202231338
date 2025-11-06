package com.mycompany.rest.api.ipc2.resources;

import dtos.salas.SalaResponse;
import dtos.salas.NewSalaRequest;
import dtos.salas.UpdateSalaRequest;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.List;
import models.salas.Sala;
import services.salas.SalasCreator;
import services.salas.SalasCrudService;

@Path("salas")
public class SalaResource {

    @Context
    UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSala(NewSalaRequest salaRequest) {
        SalasCreator salasCreator = new SalasCreator();

        try {
            Sala salaCreada = salasCreator.createSala(salaRequest);
            SalaResponse response = new SalaResponse(salaCreada);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(e.getMessage())
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error interno del servidor")
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSalas() {
        SalasCrudService salasCrudService = new SalasCrudService();
        List<SalaResponse> salas = salasCrudService.getAllSalas()
                .stream()
                .map(SalaResponse::new)
                .toList();

        return Response.ok(salas).build();
    }

    @GET
    @Path("cine/{idCine}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalasByCine(@PathParam("idCine") int idCine) {
        SalasCrudService salasCrudService = new SalasCrudService();
        List<SalaResponse> salas = salasCrudService.getSalasByCine(idCine)
                .stream()
                .map(SalaResponse::new)
                .toList();

        return Response.ok(salas).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSala(@PathParam("id") int id, UpdateSalaRequest salaRequest) {
        SalasCrudService salasCrudService = new SalasCrudService();

        try {
            Sala salaActualizada = salasCrudService.updateSala(id, salaRequest);
            SalaResponse response = new SalaResponse(salaActualizada);
            return Response.ok(response).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error interno del servidor")
                    .build();
        }
    }
    
    @GET
    @Path("{idSala}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalaById(@PathParam("idSala") int idSala) {
        SalasCrudService salasCrudService = new SalasCrudService();

        try {
            Sala sala = salasCrudService.getSalaById(idSala);
            SalaResponse response = new SalaResponse(sala);
            return Response.ok(response).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error interno del servidor")
                    .build();
        }
    }
}
