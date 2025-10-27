/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.users;

import db.UsersDB;
import dtos.users.UpdateUserRequest;
import models.users.User;
import exceptions.EntityNotFoundException;
import exceptions.UserDataInvalidException;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author andy
 */
public class UsersCrudService {
    public List<User> getAllUsers() {
        UsersDB usersDB = new UsersDB();

        //return eventsDb.getAllEvents();
        return usersDB.getAllUsers();
    }
    
    public User getUserByEmail(String correo) throws EntityNotFoundException {
        UsersDB usersDB = new UsersDB();
        Optional<User> userOpt = usersDB.getByEmail(correo);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("El usuario con correo %s no existe", correo)
            );
        }
          return userOpt.get();
    }
    
    public User updateUser(String correo, UpdateUserRequest updateUserRequest) throws UserDataInvalidException,
                EntityNotFoundException {
        UsersDB usersDB = new UsersDB();
        
        User user = getUserByEmail(correo);
        
        user.setIdUsuario(updateUserRequest.getIdUsuario());
        user.setRol(updateUserRequest.getRol());
        user.setEmail(updateUserRequest.getEmail());
        user.setPassword(updateUserRequest.getPassword());
        user.setNombreCompleto(updateUserRequest.getNombreCompleto());
        user.setEstado(updateUserRequest.getEstado());
        user.setFechaCreacion(updateUserRequest.getFechaCreacion());
        
        if (!user.isValid()) {
            throw new UserDataInvalidException("Error en los datos enviados");
        }
        
        usersDB.updateUserByEmail(correo, user);
        
        return user;
        
    }
    
    public void deleteUserByEmail(String email) throws EntityNotFoundException {
        UsersDB usersDB = new UsersDB();
        Optional<User> userOpt = usersDB.getByEmail(email);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("El Usuario con email %s no existe", email)
            );
        }
        // Obtener el ID del usuario encontrado
        User user = userOpt.get();
        int userId = user.getIdUsuario();
        usersDB.deleteUser(userId);
    }
    
    public User getUserById(int idUsuario) throws EntityNotFoundException {
    Optional<User> userOpt = new UsersDB().getById(idUsuario);
    if (userOpt.isEmpty()) {
        throw new EntityNotFoundException("Usuario no encontrado con id " + idUsuario);
    }
    return userOpt.get();
}

    
}
