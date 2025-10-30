package com.mycompany.rest.api.ipc2.resources;

import dtos.anuncios.AnuncioCompletoResponse;
import dtos.anuncios.AnuncioResponse;
import dtos.anuncios.NewAnuncioRequest;
import dtos.anuncios.UpdateAnuncioRequest;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.AnuncioDataInvalidException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import exceptions.SaldoInsuficienteException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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
import java.io.InputStream;
import java.util.List;
import models.anuncios.Anuncio;
import services.anuncios.AnunciosCreator;
import services.anuncios.AnunciosCrudService;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.MultiPart;

@Path("anuncios")
public class AnuncioResource {

    @Context
    UriInfo uriInfo;

    // ENDPOINT ORIGINAL (mantener para compatibilidad)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAnuncio(NewAnuncioRequest anuncioRequest) throws SaldoInsuficienteException, EntityNotFoundException, InvalidAmountException, InsufficientFundsException {
        AnunciosCreator anunciosCreator = new AnunciosCreator();

        try {
            anunciosCreator.createAnuncio(anuncioRequest);
            return Response.status(Response.Status.CREATED).build();
        } catch (AnuncioDataInvalidException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @POST
    @Path("con-imagen")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createAnuncioConImagen(
            @FormDataParam("id_usuario") int idUsuario,
            @FormDataParam("id_tipo_anuncio") int idTipoAnuncio,
            @FormDataParam("id_periodo") int idPeriodo,
            @FormDataParam("titulo") String titulo,
            @FormDataParam("contenido_texto") String contenidoTexto,
            @FormDataParam("video_url") String videoUrl,
            @FormDataParam("costo_total") double costoTotal,
            @FormDataParam("fecha_inicio") String fechaInicio,
            @FormDataParam("fecha_fin") String fechaFin,
            @FormDataParam("estado") String estado,
            @FormDataParam("imagen") InputStream imagenInputStream,
            @FormDataParam("imagen") FormDataContentDisposition imagenDetail) {

        AnunciosCreator anunciosCreator = new AnunciosCreator();

        try {
            // Crear el NewAnuncioRequest con los datos del formulario
            NewAnuncioRequest anuncioRequest = new NewAnuncioRequest();
            anuncioRequest.setIdUsuario(idUsuario);
            anuncioRequest.setIdTipoAnuncio(idTipoAnuncio);
            anuncioRequest.setIdPeriodo(idPeriodo);
            anuncioRequest.setTitulo(titulo);
            anuncioRequest.setContenidoTexto(contenidoTexto);
            anuncioRequest.setVideoUrl(videoUrl);
            anuncioRequest.setCostoTotal(java.math.BigDecimal.valueOf(costoTotal));

            // Parsear fechas de forma segura
            if (fechaInicio != null && !fechaInicio.isEmpty()) {
                anuncioRequest.setFechaInicio(java.time.LocalDateTime.parse(fechaInicio));
            } else {
                anuncioRequest.setFechaInicio(java.time.LocalDateTime.now());
            }

            if (fechaFin != null && !fechaFin.isEmpty()) {
                anuncioRequest.setFechaFin(java.time.LocalDateTime.parse(fechaFin));
            } else {
                // Calcular fecha fin por defecto
                anuncioRequest.setFechaFin(anuncioRequest.getFechaInicio().plusDays(7));
            }

            anuncioRequest.setEstado(estado != null ? estado : "ACTIVO");

            // Pasar la imagen al creator
            anunciosCreator.createAnuncioConImagen(anuncioRequest, imagenInputStream);
            return Response.status(Response.Status.CREATED).build();

        } catch (AnuncioDataInvalidException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (SaldoInsuficienteException e) {
            return Response.status(422).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace(); // Agregar logging
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al procesar la imagen: " + e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAnuncios() {
        AnunciosCrudService anunciosCrudService = new AnunciosCrudService();
        List<AnuncioResponse> anuncios = anunciosCrudService.getAllAnuncios()
                .stream()
                .map(AnuncioResponse::new)
                .toList();

        return Response.ok(anuncios).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnuncioById(@PathParam("id") int id) {
        AnunciosCrudService anunciosCrudService = new AnunciosCrudService();
        try {
            Anuncio existingAnuncio = anunciosCrudService.getAnuncioById(id);
            return Response.ok(new AnuncioResponse(existingAnuncio)).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // NUEVO ENDPOINT PARA OBTENER IMAGEN
    @GET
    @Path("{id}/imagen")
    @Produces("image/*")
    public Response getImagenAnuncio(@PathParam("id") int id) {
        AnunciosCrudService anunciosCrudService = new AnunciosCrudService();
        try {
            byte[] imagenBytes = anunciosCrudService.getImagenAnuncio(id);

            if (imagenBytes == null || imagenBytes.length == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            // Determinar el tipo de contenido basado en los datos (puedes almacenar el tipo en BD)
            String contentType = "image/jpeg"; // O determinar dinámicamente

            return Response.ok(imagenBytes)
                    .type(contentType)
                    .header("Content-Disposition", "inline; filename=\"imagen-anuncio-" + id + "\"")
                    .build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAnuncio(@PathParam("id") int id, UpdateAnuncioRequest anuncioRequest) {
        AnunciosCrudService anunciosCrudService = new AnunciosCrudService();

        try {
            Anuncio anuncioUpdated = anunciosCrudService.updateAnuncio(id, anuncioRequest);
            return Response.ok(new AnuncioResponse(anuncioUpdated)).build();
        } catch (AnuncioDataInvalidException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // NUEVO ENDPOINT PARA ACTUALIZAR CON IMAGEN
    @PUT
    @Path("{id}/con-imagen")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateAnuncioConImagen(
            @PathParam("id") int id,
            @FormDataParam("titulo") String titulo,
            @FormDataParam("contenido_texto") String contenidoTexto,
            @FormDataParam("video_url") String videoUrl,
            @FormDataParam("estado") String estado,
            @FormDataParam("imagen") InputStream imagenInputStream,
            @FormDataParam("imagen") FormDataContentDisposition imagenDetail) {

        AnunciosCrudService anunciosCrudService = new AnunciosCrudService();

        try {
            // Crear UpdateAnuncioRequest
            UpdateAnuncioRequest anuncioRequest = new UpdateAnuncioRequest();
            anuncioRequest.setTitulo(titulo);
            anuncioRequest.setContenidoTexto(contenidoTexto);
            anuncioRequest.setVideoUrl(videoUrl);
            anuncioRequest.setEstado(estado);

            Anuncio anuncioUpdated = anunciosCrudService.updateAnuncioConImagen(id, anuncioRequest, imagenInputStream);
            return Response.ok(new AnuncioResponse(anuncioUpdated)).build();

        } catch (AnuncioDataInvalidException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al procesar la imagen").build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteAnuncio(@PathParam("id") int id) {
        AnunciosCrudService anunciosCrudService = new AnunciosCrudService();

        try {
            anunciosCrudService.deleteAnuncio(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // Endpoints específicos para usuario
    @GET
    @Path("usuario/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnunciosByUsuario(@PathParam("idUsuario") int idUsuario) {
        AnunciosCrudService anunciosCrudService = new AnunciosCrudService();
        List<AnuncioResponse> anuncios = anunciosCrudService.getAnunciosByUsuario(idUsuario)
                .stream()
                .map(AnuncioResponse::new)
                .toList();

        return Response.ok(anuncios).build();
    }

    @GET
    @Path("usuario/{idUsuario}/completos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnunciosCompletosByUsuario(@PathParam("idUsuario") int idUsuario) {
        AnunciosCrudService anunciosCrudService = new AnunciosCrudService();
        List<AnuncioCompletoResponse> anuncios = anunciosCrudService.getAnunciosCompletosByUsuario(idUsuario)
                .stream()
                .map(AnuncioCompletoResponse::new)
                .toList();

        return Response.ok(anuncios).build();
    }

    @GET
    @Path("{id}/completo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnuncioCompleto(@PathParam("id") int id) {
        AnunciosCrudService anunciosCrudService = new AnunciosCrudService();
        try {
            Anuncio existingAnuncio = anunciosCrudService.getAnuncioCompletoById(id);
            return Response.ok(new AnuncioCompletoResponse(existingAnuncio)).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
