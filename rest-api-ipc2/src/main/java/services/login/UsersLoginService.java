/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.login;

import db.UsersDB;
import exceptions.EntityNotFoundException;
import java.util.Optional;
import models.users.PasswordHasher;
import models.users.User;

/**
 *
 * @author andy
 */
public class UsersLoginService {
    
    public User login(String email, String password) throws EntityNotFoundException {
        UsersDB usersDB = new UsersDB();
        
        // Buscar usuario por email
        Optional<User> userOpt = usersDB.getByEmail(email);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException("Credenciales incorrectas");
        }
        
        User user = userOpt.get();
        
        // Verificar contraseña usando PasswordHasher (ENCRIPTADA)
        if (!PasswordHasher.verifyPassword(password, user.getPassword())) {
            throw new EntityNotFoundException("Credenciales incorrectas");
        }
        
        // Verificar estado del usuario
        if (!"ACTIVO".equals(user.getEstado())) {
            throw new EntityNotFoundException("Usuario inactivo. Contacte al administrador.");
        }
        
        return user;
    }
}