/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.users;

import db.UsersDB;
import dtos.users.NewUserRequest;
import models.users.User;
import exceptions.EntityAlreadyExistsException;
import exceptions.UserDataInvalidException;

/**
 *
 * @author andy
 */
public class UsersCreator {
    public User createUser(NewUserRequest newUserRequest) throws UserDataInvalidException,
            EntityAlreadyExistsException {
        UsersDB usersDB = new UsersDB();
        User user = extractUser(newUserRequest);
        if (usersDB.existsUser(newUserRequest.getIdUsuario())) {
            throw new EntityAlreadyExistsException(
                    String.format("El usuario con id %s ya existe", user.getIdUsuario()));
        }
        
        usersDB.createUser(user);
        
        return user;
    }
    
    private User extractUser(NewUserRequest newUserRequest) throws UserDataInvalidException {
        try {
            User user = new User(
                    newUserRequest.getIdUsuario(),
                    newUserRequest.getRol(),
                    newUserRequest.getEmail(),
                    newUserRequest.getPassword(),
                    newUserRequest.getNombreCompleto(),
                    newUserRequest.getEstado(),
                    newUserRequest.getFechaCreacion()
            );
            
            if (!user.isValid()) {
                throw new UserDataInvalidException("Error en los datos enviados");
            }
            
            return user;
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new UserDataInvalidException("Error en los datos enviados");
        }
    }
}