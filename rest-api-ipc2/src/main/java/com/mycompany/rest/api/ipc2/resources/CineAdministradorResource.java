package com.mycompany.rest.api.ipc2.resources;

import dtos.cines.CineResponse;
import exceptions.EntityNotFoundException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import services.cines.CineAdministradorService;

/**
 *
 * @author andy
 */
@Path("cine-admin")
public class CineAdministradorResource {
    
    @GET
    @Path("{idUsuario}/cines")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCinesByAdministrador(@PathParam("idUsuario") int idUsuario) {
        CineAdministradorService cineAdminService = new CineAdministradorService();
        
        try {
            List<CineResponse> cines = cineAdminService.getCinesByAdministrador(idUsuario)
                    .stream()
                    .map(CineResponse::new)
                    .toList();
            
            return Response.ok(cines).build();
            
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontraron cines para el administrador con ID: " + idUsuario)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}