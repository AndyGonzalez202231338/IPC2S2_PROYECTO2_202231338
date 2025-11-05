package com.mycompany.rest.api.ipc2.resources;

import dtos.funciones.NewFuncionRequest;
import dtos.funciones.FuncionResponse;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.InvalidAmountException;
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
import models.funcion.Funcion;

import services.funciones.FuncionesCrudService;
import services.funciones.FuncionesCreator;

@Path("funciones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FuncionResource {

    @Context
    UriInfo uriInfo;

    /**
     * Crea una nueva función en la base de datos
     */
    @POST
    public Response createFuncion(NewFuncionRequest funcionRequest) {
        FuncionesCreator funcionesCreator = new FuncionesCreator();

        try {
            funcionesCreator.createFuncion(funcionRequest);
            return Response.status(Response.Status.CREATED).build();
        } catch (UserDataInvalidException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Datos inválidos").build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).entity("La función ya existe o ya hay una funcion en este horario").build();
        }
    }

    /**
     * Obtiene todas las funciones registradas
     */
    @GET
    public Response getAllFunciones() {
        FuncionesCrudService funcionesCrudService = new FuncionesCrudService();
        List<FuncionResponse> funciones = funcionesCrudService.getAllFunciones()
                .stream()
                .map(FuncionResponse::new)
                .toList();

        return Response.ok(funciones).build();
    }

    /**
     * Obtiene una función por su ID
     */
    @GET
    @Path("{idFuncion}")
    public Response getFuncionById(@PathParam("idFuncion") int idFuncion) {
        FuncionesCrudService funcionesCrudService = new FuncionesCrudService();
        try {
            Funcion funcion = funcionesCrudService.getFuncionById(idFuncion);
            return Response.ok(new FuncionResponse(funcion)).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Función no encontrada").build();
        }
    }

    /**
     * Obtiene todas las funciones de una sala específica
     */
    @GET
    @Path("sala/{idSala}")
    public Response getFuncionesBySala(@PathParam("idSala") int idSala) {
        FuncionesCrudService funcionesCrudService = new FuncionesCrudService();
        List<FuncionResponse> funciones = funcionesCrudService.getfuncionesBySala(idSala)
                .stream()
                .map(FuncionResponse::new)
                .toList();

        return Response.ok(funciones).build();
    }

    /**
     * Obtiene todas las funciones de un cine (JOIN con tabla sala)
     */
    @GET
    @Path("cine/{idCine}")
    public Response getFuncionesByCine(@PathParam("idCine") int idCine) {
        FuncionesCrudService funcionesCrudService = new FuncionesCrudService();
        List<FuncionResponse> funciones = funcionesCrudService.getFuncionesByCine(idCine)
                .stream()
                .map(FuncionResponse::new)
                .toList();

        return Response.ok(funciones).build();
    }
}
