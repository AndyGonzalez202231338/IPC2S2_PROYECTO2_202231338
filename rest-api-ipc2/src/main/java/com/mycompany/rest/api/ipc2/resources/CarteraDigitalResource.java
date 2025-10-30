/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rest.api.ipc2.resources;

import dtos.operacionCartera.CarteraResponse;
import dtos.operacionCartera.CompraRequest;
import dtos.operacionCartera.DepositoRequest;
import exceptions.EntityNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import services.users.CarteraDigitalService;


/**
 *
 * @author andy
 */
@Path("wallet")
public class CarteraDigitalResource {
    
    private CarteraDigitalService carteraService;
    
    public CarteraDigitalResource() {
        this.carteraService = new CarteraDigitalService();
    }
    
    @GET
    @Path("user/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarSaldo(@PathParam("idUsuario") int idUsuario) {
        try {
            var cartera = carteraService.obtenerCartera(idUsuario);
            CarteraResponse response = new CarteraResponse(
                cartera.getIdCartera(),
                cartera.getIdUsuario(),
                cartera.getSaldo()
            );
            return Response.ok(response).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cartera no encontrada")
                    .build();
        }
    }
    
    @POST
    @Path("user/{idUsuario}/deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response depositar(@PathParam("idUsuario") int idUsuario, DepositoRequest request) {
        try {
            var cartera = carteraService.depositar(idUsuario, request.getMonto());
            CarteraResponse response = new CarteraResponse(
                cartera.getIdCartera(),
                cartera.getIdUsuario(),
                cartera.getSaldo()
            );
            return Response.ok(response).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuario no encontrado")
                    .build();
        } catch (InvalidAmountException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Monto inválido")
                    .build();
        }
    }
    
    @POST
    @Path("user/{idUsuario}/purchase")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response realizarCompra(@PathParam("idUsuario") int idUsuario, CompraRequest request) {
        try {
            var cartera = carteraService.realizarCompra(idUsuario, request.getMonto());
            CarteraResponse response = new CarteraResponse(
                cartera.getIdCartera(),
                cartera.getIdUsuario(),
                cartera.getSaldo()
            );
            return Response.ok(response).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuario no encontrado")
                    .build();
        } catch (InvalidAmountException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Monto inválido")
                    .build();
        } catch (InsufficientFundsException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Saldo insuficiente")
                    .build();
        }
    }
}