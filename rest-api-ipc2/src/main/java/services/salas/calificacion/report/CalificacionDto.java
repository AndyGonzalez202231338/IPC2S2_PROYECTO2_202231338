/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.salas.calificacion.report;

import java.time.LocalDateTime;

/**
 *
 * @author andy
 */
public class CalificacionDto {
    private String nombreUsuario;
    private Integer calificacion;
    private LocalDateTime fechaCalificacion;
    
    public CalificacionDto(String nombreUsuario, Integer calificacion, LocalDateTime fechaCalificacion) {
        this.nombreUsuario = nombreUsuario;
        this.calificacion = calificacion;
        this.fechaCalificacion = fechaCalificacion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public LocalDateTime getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(LocalDateTime fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }
    
    
    
    
}
