/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rest.api.ipc2.resources;

import dtos.operacionCarteraCine.CarteraCineResponse;
import dtos.operacionCarteraCine.DepositoCineRequest;
import dtos.operacionCarteraCine.PagoRequest;
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
import services.cines.CarteraCineService;


/**
 *
 * @author andy
 */
@Path("wallet-cine")
public class CarteraCineResource {
    
    private CarteraCineService carteraCineService;
    
    public CarteraCineResource() {
        this.carteraCineService = new CarteraCineService();
    }
    
    @GET
    @Path("cine/{idCine}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarSaldoCine(@PathParam("idCine") int idCine) {
        try {
            var cartera = carteraCineService.obtenerCarteraCine(idCine);
            CarteraCineResponse response = new CarteraCineResponse(
                cartera.getIdCarteraCine(),
                cartera.getIdCine(),
                cartera.getSaldo()
            );
            return Response.ok(response).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cartera del cine no encontrada")
                    .build();
        }
    }
    
    @POST
    @Path("cine/{idCine}/deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response depositarCine(@PathParam("idCine") int idCine, DepositoCineRequest request) {
        try {
            var cartera = carteraCineService.depositarCine(idCine, request.getMonto());
            CarteraCineResponse response = new CarteraCineResponse(
                cartera.getIdCarteraCine(),
                cartera.getIdCine(),
                cartera.getSaldo()
            );
            return Response.ok(response).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cine no encontrado")
                    .build();
        } catch (InvalidAmountException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Monto inválido")
                    .build();
        }
    }
    
    @POST
    @Path("cine/{idCine}/receive-payment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response recibirPago(@PathParam("idCine") int idCine, PagoRequest request) {
        try {
            var cartera = carteraCineService.recibirPago(idCine, request.getMonto());
            CarteraCineResponse response = new CarteraCineResponse(
                cartera.getIdCarteraCine(),
                cartera.getIdCine(),
                cartera.getSaldo()
            );
            return Response.ok(response).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cine no encontrado")
                    .build();
        } catch (InvalidAmountException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Monto inválido")
                    .build();
        }
    }
    
    @POST
    @Path("cine/{idCine}/pay-expense")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response pagarGasto(@PathParam("idCine") int idCine, PagoRequest request) {
        try {
            var cartera = carteraCineService.pagarGasto(idCine, request.getMonto());
            CarteraCineResponse response = new CarteraCineResponse(
                cartera.getIdCarteraCine(),
                cartera.getIdCine(),
                cartera.getSaldo()
            );
            return Response.ok(response).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cine no encontrado")
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
