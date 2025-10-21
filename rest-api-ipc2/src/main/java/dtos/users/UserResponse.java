/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import models.users.Role;
import models.users.User;

/**
 *
 * @author andy
 */
public class UserResponse {
    private int idUsuario;
    private Role rol;
    private String email;
    private String password;
    private String nombreCompleto;
    private String estado;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacion;
    
    public UserResponse(User user){
        this.idUsuario = user.getIdUsuario();
        this.rol = user.getRol();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nombreCompleto = user.getNombreCompleto();
        this.estado = user.getEstado();
        this.fechaCreacion = user.getFechaCreacion();
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Role getRol() {
        return rol;
    }

    public void setRol(Role rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    
}
