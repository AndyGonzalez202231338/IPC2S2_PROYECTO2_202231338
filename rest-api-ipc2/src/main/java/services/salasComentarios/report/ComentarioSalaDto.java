/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.salasComentarios.report;

import java.time.LocalDateTime;

/**
 *
 * @author andy
 */
public class ComentarioSalaDto {
    private String nombreSala;
    private String nombreUsuario;
    private String comentario;
    private LocalDateTime fechaComentario;
    private Integer calificacion;
    
    public ComentarioSalaDto(String nombreSala, String nombreUsuario, String comentario, 
                           LocalDateTime fechaComentario, Integer calificacion) {
        this.nombreSala = nombreSala;
        this.nombreUsuario = nombreUsuario;
        this.comentario = comentario;
        this.fechaComentario = fechaComentario;
        this.calificacion = calificacion;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaComentario() {
        return fechaComentario;
    }

    public void setFechaComentario(LocalDateTime fechaComentario) {
        this.fechaComentario = fechaComentario;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }
    
    
}
