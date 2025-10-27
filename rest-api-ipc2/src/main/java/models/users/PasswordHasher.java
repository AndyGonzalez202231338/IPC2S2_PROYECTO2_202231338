/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.users;


import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author andy
 */
public class PasswordHasher {
    
    // Número de rondas de hashing
    private static final int ROUNDS = 12;
    
    /**
     * Hashea una contraseña usando BCrypt
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(ROUNDS));
    }
    
    /**
     * Verifica si una contraseña en texto plano coincide con el hash almacenado
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
