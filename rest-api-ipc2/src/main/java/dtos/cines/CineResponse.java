/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos.cines;

import java.time.LocalDateTime;
import java.util.List;
import models.cines.Cine;

/**
 *
 * @author andy
 */
public class CineResponse {
    private Integer idCine;
    private String nombre;
    private String direccion;
    private LocalDateTime fechaCreacion;
    private String estado;
    private List<Integer> administradores;

    public CineResponse(Cine cine) {
        this.idCine = cine.getIdCine();
        this.nombre = cine.getNombre();
        this.direccion = cine.getDireccion();
        this.estado = cine.getEstado();
        this.fechaCreacion = cine.getFechaCreacion();
        this.administradores = cine.getAdministradores();
    }
    

    public Integer getIdCine() {
        return idCine;
    }

    public void setIdCine(Integer idCine) {
        this.idCine = idCine;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Integer> getAdministradores() {
        return administradores;
    }

    public void setAdministradores(List<Integer> administradores) {
        this.administradores = administradores;
    }
    
    
}
