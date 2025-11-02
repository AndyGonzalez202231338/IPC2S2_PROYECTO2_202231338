package com.mycompany.rest.api.ipc2.resources;

import dtos.anuncios.AnuncioCompletoResponse;
import dtos.publicidad.PublicidadResponse;
import dtos.publicidad.CrearPublicidadRequest;
import exceptions.EntityNotFoundException;
import exceptions.InvalidAmountException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import models.publicidad.Publicidad;
import services.publicidad.PublicidadService;

/**
 *
 * @author andy
 */
@Path("publicidades")
public class PublicidadResource {

    private final PublicidadService publicidadService;

    public PublicidadResource() {
        this.publicidadService = new PublicidadService();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearPublicidad(CrearPublicidadRequest request) {
        try {
            var publicidad = publicidadService.crearPublicidad(
                    request.getIdAnuncio(),
                    request.getIdUsuario(),
                    request.getPrecioBloqueo()
            );

            PublicidadResponse response = new PublicidadResponse(publicidad);

            return Response.status(Response.Status.CREATED).entity(response).build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Anuncio o usuario no encontrado")
                    .build();
        } catch (InvalidAmountException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El precio de bloqueo debe ser mayor a 0")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear la publicidad")
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPublicidades() {
        try {
            List<PublicidadResponse> publicidades = publicidadService.getAllPublicidades()
                    .stream()
                    .map(PublicidadResponse::new)
                    .toList();

            return Response.ok(publicidades).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("anuncio/{idAnuncio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublicidadByAnuncioId(@PathParam("idAnuncio") int idAnuncio) {
        try {
            Optional<Publicidad> publicidadOpt = publicidadService.getPublicidadByAnuncioId(idAnuncio);

            if (publicidadOpt.isPresent()) {
                Publicidad publicidad = publicidadOpt.get();
                PublicidadResponse response = new PublicidadResponse(publicidad);
                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    

}
