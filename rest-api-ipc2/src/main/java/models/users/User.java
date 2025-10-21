/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.users;

import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author andy
 */
import java.time.LocalDateTime;

public class User {
    private int idUsuario;
    private Role rol;
    private String email;
    private String password;
    private String nombreCompleto;
    private String estado; // "ACTIVO" o "INACTIVO"
    private LocalDateTime fechaCreacion;

    public User(int idUsuario, Role rol, String email, String password,
                String nombreCompleto, String estado, LocalDateTime fechaCreacion) {
        this.idUsuario = idUsuario;
        this.rol = rol;
        this.email = email;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
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

    

    @Override
    public String toString() {
        return "User{" +
                "idUsuario=" + idUsuario +
                ", rol=" + rol.getNombreRol() +
                ", email='" + email + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
    
    public boolean isValid() {
    return rol != null
            && StringUtils.isNotBlank(email)
            && StringUtils.isNotBlank(password)
            && StringUtils.isNotBlank(nombreCompleto)
            && StringUtils.isNotBlank(estado)
            && fechaCreacion != null;
    }

}

