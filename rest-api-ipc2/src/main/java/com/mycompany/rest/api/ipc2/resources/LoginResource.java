/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rest.api.ipc2.resources;

import dtos.login.LoginRequest;
import dtos.login.LoginResponse;
import exceptions.EntityNotFoundException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import services.login.UsersLoginService;

/**
 *
 * @author andy
 */
@Path("auth")
public class LoginResource {
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {
        UsersLoginService usersLoginService = new UsersLoginService();
        
        try {
            // Validar que lleguen los datos necesarios
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(LoginResponse.error("El email es requerido"))
                    .build();
            }

            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(LoginResponse.error("La contraseña es requerida"))
                    .build();
            }
            
            // Intentar hacer login
            var user = usersLoginService.login(loginRequest.getEmail(), loginRequest.getPassword());
            
            // Login exitoso
            return Response.ok(LoginResponse.success(user)).build();
            
        } catch (EntityNotFoundException e) {
            // Credenciales incorrectas o usuario inactivo
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(LoginResponse.error(e.getMessage()))
                .build();
        } catch (Exception e) {
            // Error interno del servidor
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(LoginResponse.error("Error interno del servidor"))
                .build();
        }
    }
    
    // Endpoint para verificar que el servicio está funcionando
    @POST
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test() {
        return Response.ok(LoginResponse.success(null)).build();
    }
}
