/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos.login;

import models.users.User;

/**
 *
 * @author andy
 */
public class LoginResponse {
    private boolean success;
    private User user;
    private String message;

    // Constructores
    public LoginResponse() {}

    public LoginResponse(boolean success, User user, String message) {
        this.success = success;
        this.user = user;
        this.message = message;
    }

    // Métodos estáticos para crear respuestas fácilmente
    public static LoginResponse success(User user) {
        return new LoginResponse(true, user, "Login exitoso");
    }

    public static LoginResponse error(String message) {
        return new LoginResponse(false, null, message);
    }

    // Getters y Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
