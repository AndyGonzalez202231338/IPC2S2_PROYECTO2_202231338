package models.cines;

import models.users.User;
import java.time.LocalDateTime;
import java.util.List;

public class Cine {
    private Integer idCine;
    private String nombre;
    private String direccion;
    private LocalDateTime fechaCreacion;
    private String estado;
    private List<Integer> administradores; // IDs de usuarios

    // Constructores
    public Cine() {}

    public Cine(String nombre, String direccion, List<Integer> administradores) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.administradores = administradores;
        this.estado = "ACTIVO";
        this.fechaCreacion = LocalDateTime.now();
    }

    public Cine(Integer idCine, String nombre, String direccion, LocalDateTime fechaCreacion, String estado, List<Integer> administradores) {
        this.idCine = idCine;
        this.nombre = nombre;
        this.direccion = direccion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.administradores = administradores;
    }

    

    // Getters y Setters
    public Integer getIdCine() { return idCine; }
    public void setIdCine(Integer idCine) { this.idCine = idCine; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public List<Integer> getAdministradores() { return administradores; }
    public void setAdministradores(List<Integer> administradores) { this.administradores = administradores; }

    public boolean isValid() {
        return nombre != null && !nombre.trim().isEmpty() &&
               direccion != null && !direccion.trim().isEmpty() &&
               estado != null && !estado.trim().isEmpty();
    }
}