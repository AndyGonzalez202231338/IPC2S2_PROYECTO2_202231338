/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rest.api.ipc2.resources;

import dtos.users.NewUserRequest;
import dtos.users.UpdateUserRequest;
import dtos.users.UserResponse;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.UserDataInvalidException;
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
import models.users.User;
import services.users.UsersCreator;
import services.users.UsersCrudService;

/**
 *
 * @author andy
 */
@Path("users")
public class UsersResource {

    @Context
    UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEvent(NewUserRequest userRequest) {
        UsersCreator usersCreator = new UsersCreator();

        try {

            usersCreator.createUser(userRequest);

            return Response.status(Response.Status.CREATED).build();
        } catch (UserDataInvalidException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEvents() {
        UsersCrudService userCrudService = new UsersCrudService();
        List<UserResponse> users = userCrudService.getAllUsers()
                .stream()
                .map(UserResponse::new)
                .toList();

        return Response.ok(users).build();
    }

    @GET
    @Path("{correo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventByEmail(@PathParam("correo") String correo) {
        UsersCrudService userCrudService = new UsersCrudService();
        try {
            User existingEvent = userCrudService.getUserByEmail(correo);

            return Response.ok(new UserResponse(existingEvent)).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int idUsuario) {
        UsersCrudService userCrudService = new UsersCrudService();
        try {
            User user = userCrudService.getUserById(idUsuario); // nuevo método en tu servicio
            return Response.ok(new UserResponse(user)).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("{correo}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEvent(@PathParam("correo") String correo,
            UpdateUserRequest userRequest) {
        UsersCrudService userCrudService = new UsersCrudService();

        try {

            User userUpdated = userCrudService.updateUser(correo, userRequest);

            return Response.ok(new UserResponse(userUpdated)).build();
        } catch (UserDataInvalidException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{correo}")
    public Response deleteEvent(@PathParam("correo") String email) {
        UsersCrudService userCrudService = new UsersCrudService();

        try {

            userCrudService.deleteUserByEmail(email);

            return Response.ok().build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
