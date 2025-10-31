package com.mycompany.rest.api.ipc2.resources;

import dtos.movies.CategoriaPeliculaResponse;
import dtos.movies.MovieResponse;
import dtos.movies.NewMovieRequest;
import dtos.movies.UpdateMovieRequest;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MovieDataInvalidException;
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
import java.util.List;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import models.movies.Movies;
import services.Movies.MoviesCreator;
import services.Movies.MoviesCrudService;

/**
 *
 * @author andy
 */
@Path("movies")
public class PeliculaResource {

    @Context
    UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMovies() {
        MoviesCrudService moviesCrudService = new MoviesCrudService();
        List<MovieResponse> peliculas = moviesCrudService.getAllMovies()
                .stream()
                .map(MovieResponse::new)
                .toList();

        return Response.ok(peliculas).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieById(@PathParam("id") int id) {
        MoviesCrudService moviesCrudService = new MoviesCrudService();
        try {
            Movies existingMovie = moviesCrudService.getMovieById(id);
            return Response.ok(new MovieResponse(existingMovie)).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("{id}/categorias")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoriasByPelicula(@PathParam("id") int id) {
        MoviesCrudService moviesCrudService = new MoviesCrudService();
        try {
            List<CategoriaPeliculaResponse> categorias = moviesCrudService.getCategoriasByMovieId(id)
                    .stream()
                    .map(CategoriaPeliculaResponse::new)
                    .toList();
            return Response.ok(categorias).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createPelicula(@FormDataParam("titulo") String titulo,
            @FormDataParam("sinopsis") String sinopsis,
            @FormDataParam("duracionMinutos") String duracionMinutosStr,
            @FormDataParam("director") String director,
            @FormDataParam("reparto") String reparto,
            @FormDataParam("clasificacion") String clasificacion,
            @FormDataParam("fechaEstreno") String fechaEstreno,
            @FormDataParam("estado") String estado,
            @FormDataParam("posterFile") InputStream posterFileInputStream,
            @FormDataParam("posterFile") FormDataContentDisposition fileDetail,
            @FormDataParam("categorias") List<String> categoriasStr) {

        MoviesCreator moviesCreator = new MoviesCreator();

        try {
            // Convertir tipos
            int duracionMinutos = Integer.parseInt(duracionMinutosStr);

            // Convertir categorías de String a Integer
            List<Integer> categorias = new ArrayList<>();
            if (categoriasStr != null) {
                for (String catStr : categoriasStr) {
                    categorias.add(Integer.parseInt(catStr));
                }
            }

            // Convertir el InputStream a Byte[]
            Byte[] posterBytes = null;
            if (posterFileInputStream != null) {
                byte[] fileBytes = posterFileInputStream.readAllBytes();
                posterBytes = new Byte[fileBytes.length];
                for (int i = 0; i < fileBytes.length; i++) {
                    posterBytes[i] = fileBytes[i];
                }
            }

            NewMovieRequest movieRequest = new NewMovieRequest(
                    0, // idPelicula se generará automáticamente
                    titulo,
                    sinopsis,
                    duracionMinutos,
                    director,
                    reparto,
                    clasificacion,
                    java.sql.Date.valueOf(fechaEstreno),
                    posterBytes,
                    estado
            );

            moviesCreator.createMovieWithCategories(movieRequest, categorias);

            return Response.status(Response.Status.CREATED).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error en formato de números: " + e.getMessage())
                    .build();
        } catch (MovieDataInvalidException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error interno: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("{id}/estado")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMovieEstado(@PathParam("id") int id,
            Map<String, String> estadoRequest) {
        MoviesCrudService moviesCrudService = new MoviesCrudService();

        try {
            String nuevoEstado = estadoRequest.get("estado");

            if (nuevoEstado == null || (!nuevoEstado.equals("ACTIVA") && !nuevoEstado.equals("INACTIVA"))) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Estado inválido. Debe ser 'ACTIVA' o 'INACTIVA'")
                        .build();
            }

            Movies movieUpdated = moviesCrudService.updateMovieEstado(id, nuevoEstado);
            return Response.ok(new MovieResponse(movieUpdated)).build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePelicula(@PathParam("id") int id,
            UpdateMovieRequest movieRequest) {
        MoviesCrudService moviesCrudService = new MoviesCrudService();

        try {
            Movies movieUpdated = moviesCrudService.updateMovie(id, movieRequest);
            return Response.ok(new MovieResponse(movieUpdated)).build();
        } catch (MovieDataInvalidException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deletePelicula(@PathParam("id") int id) {
        MoviesCrudService moviesCrudService = new MoviesCrudService();

        try {
            moviesCrudService.deleteMovie(id);
            return Response.noContent().build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
