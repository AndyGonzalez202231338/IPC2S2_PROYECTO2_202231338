/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rest.api.ipc2.resources;

import dtos.bloqueo.BloqueoPublicidadResponse;
import dtos.bloqueo.NewBloqueoRequest;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
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
import java.math.BigDecimal;
import java.util.List;
import models.bloqueo.BloqueoPublicidad;
import services.bloqueo.BloqueoPublicidadCreator;
import services.bloqueo.BloqueoPublicidadCrudService;
import services.cines.CarteraCineService;

/**
 *
 * @author andy
 */
@Path("bloqueo-publicidad")
public class BloqueoAnuncioResource {

    @Context
    UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBloqueo(NewBloqueoRequest bloqueoRequest) {
        BloqueoPublicidadCreator bloqueoCreator = new BloqueoPublicidadCreator();
        CarteraCineService carteraCineService = new CarteraCineService();

        try {
            bloqueoCreator.createBloqueo(bloqueoRequest);
            carteraCineService.pagarGasto(bloqueoRequest.getIdCine(), bloqueoRequest.getCostoTotal());
            return Response.status(Response.Status.CREATED).build();
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
    public Response getAllBloqueos() {
        BloqueoPublicidadCrudService bloqueoCrudService = new BloqueoPublicidadCrudService();
        List<BloqueoPublicidadResponse> bloqueos = bloqueoCrudService.getAllBloqueos()
                .stream()
                .map(BloqueoPublicidadResponse::new)
                .toList();

        return Response.ok(bloqueos).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBloqueoById(@PathParam("id") int idBloqueo) {
        BloqueoPublicidadCrudService bloqueoCrudService = new BloqueoPublicidadCrudService();
        try {
            BloqueoPublicidad bloqueo = bloqueoCrudService.getBloqueoById(idBloqueo);
            return Response.ok(new BloqueoPublicidadResponse(bloqueo)).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("anuncio/{idAnuncio}/cine/{idCine}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBloqueoByAnuncioAndCine(
            @PathParam("idAnuncio") int idAnuncio,
            @PathParam("idCine") int idCine) {
        BloqueoPublicidadCrudService bloqueoCrudService = new BloqueoPublicidadCrudService();
        try {
            BloqueoPublicidad bloqueo = bloqueoCrudService.getBloqueoByAnuncioAndCine(idAnuncio, idCine);
            return Response.ok(new BloqueoPublicidadResponse(bloqueo)).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("cine/{idCine}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBloqueosByCine(@PathParam("idCine") int idCine) {
        BloqueoPublicidadCrudService bloqueoCrudService = new BloqueoPublicidadCrudService();
        List<BloqueoPublicidadResponse> bloqueos = bloqueoCrudService.getBloqueosByCine(idCine)
                .stream()
                .map(BloqueoPublicidadResponse::new)
                .toList();

        return Response.ok(bloqueos).build();
    }

    @GET
    @Path("publicidad/{idPublicidad}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBloqueosByPublicidad(@PathParam("idPublicidad") int idPublicidad) {
        BloqueoPublicidadCrudService bloqueoCrudService = new BloqueoPublicidadCrudService();
        List<BloqueoPublicidadResponse> bloqueos = bloqueoCrudService.getBloqueosByPublicidad(idPublicidad)
                .stream()
                .map(BloqueoPublicidadResponse::new)
                .toList();

        return Response.ok(bloqueos).build();
    }

    @GET
    @Path("anuncio/{idAnuncio}/activos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBloqueosActivosByAnuncio(@PathParam("idAnuncio") int idAnuncio) {
        BloqueoPublicidadCrudService bloqueoCrudService = new BloqueoPublicidadCrudService();
        List<BloqueoPublicidadResponse> bloqueos = bloqueoCrudService.getBloqueosActivosByAnuncio(idAnuncio)
                .stream()
                .map(BloqueoPublicidadResponse::new)
                .toList();

        return Response.ok(bloqueos).build();
    }

    @GET
    @Path("cine/{idCine}/activos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBloqueosActivosByCine(@PathParam("idCine") int idCine) {
        BloqueoPublicidadCrudService bloqueoCrudService = new BloqueoPublicidadCrudService();
        List<BloqueoPublicidadResponse> bloqueos = bloqueoCrudService.getBloqueosActivosByCine(idCine)
                .stream()
                .map(BloqueoPublicidadResponse::new)
                .toList();

        return Response.ok(bloqueos).build();
    }

    @GET
    @Path("anuncio/{idAnuncio}/disponible")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verificarDisponibilidadAnuncio(@PathParam("idAnuncio") int idAnuncio) {
        BloqueoPublicidadCrudService bloqueoCrudService = new BloqueoPublicidadCrudService();
        try {
            boolean disponible = bloqueoCrudService.verificarDisponibilidadAnuncio(idAnuncio);
            return Response.ok(disponible).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}