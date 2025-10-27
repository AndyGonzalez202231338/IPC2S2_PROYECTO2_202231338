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
import models.users.PasswordHasher;

/**
 *
 * @author andy
 */
public class UsersCreator {
    // En UsersCreator.java, modifica el método createUser:
public User createUser(NewUserRequest newUserRequest) throws UserDataInvalidException,
        EntityAlreadyExistsException {
    UsersDB usersDB = new UsersDB();
    User user = extractUser(newUserRequest);
    
    if (usersDB.existsUser(newUserRequest.getIdUsuario())) {
        throw new EntityAlreadyExistsException(
                String.format("El usuario con id %s ya existe", user.getIdUsuario()));
    }
    
    // Crear el usuario
    usersDB.createUser(user);
    
    // CREAR CARTERA DIGITAL AUTOMÁTICAMENTE
    CarteraDigitalService carteraService = new CarteraDigitalService();
    carteraService.crearCarteraParaUsuario(user.getIdUsuario());
    
    return user;
}
    
    private User extractUser(NewUserRequest newUserRequest) throws UserDataInvalidException {
        try {
            // Encriptar la contraseña antes de crear el usuario
            String hashedPassword = PasswordHasher.hashPassword(newUserRequest.getPassword());
            
            User user = new User(
                    newUserRequest.getIdUsuario(),
                    newUserRequest.getRol(),
                    newUserRequest.getEmail(),
                    hashedPassword, // Usar la contraseña encriptada
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