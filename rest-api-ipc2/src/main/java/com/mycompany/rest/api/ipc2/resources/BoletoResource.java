package com.mycompany.rest.api.ipc2.resources;

import dtos.boletos.BoletoResponse;
import dtos.boletos.NewBoletoRequest;
import dtos.salas.SalaResponse;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import exceptions.SaldoInsuficienteException;
import exceptions.UserDataInvalidException;

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
import models.Boleto;
import models.salas.Sala;
import services.boletos.BoletoCrudService;
import services.boletos.BoletosCreator;


@Path("boletos")
public class BoletoResource {

    @Context
    UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBoleto(NewBoletoRequest NewBoletoRequest) throws SaldoInsuficienteException, EntityNotFoundException, InvalidAmountException, InsufficientFundsException {
        BoletosCreator boletosCreator = new BoletosCreator();
        try {
            
            boletosCreator.createBoleto(NewBoletoRequest);
            return Response.status(Response.Status.CREATED).build();
        } catch (UserDataInvalidException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
    
    // Endpoint existente para obtener todos los boletos
    @GET
    @Path("/usuario/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBoletosByUsuario(@PathParam("idUsuario") int idUsuario) {
        BoletoCrudService boletoCrudService = new BoletoCrudService();
        try {
            List<Boleto> boletos = boletoCrudService.getBoletosByUsuario(idUsuario);
            return Response.ok(boletos).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("/usuario/{idUsuario}/unicos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBoletosUnicosByUsuario(@PathParam("idUsuario") int idUsuario) {
        BoletoCrudService boletoCrudService = new BoletoCrudService();
        try {
            List<Boleto> boletosUnicos = boletoCrudService.getBoletosUnicosByUsuario(idUsuario);
            return Response.ok(boletosUnicos).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
