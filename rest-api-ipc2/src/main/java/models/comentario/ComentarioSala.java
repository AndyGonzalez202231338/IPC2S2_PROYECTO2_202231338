/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.comentario;

import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author andy
 */
public class ComentarioSala {
    private int idComentarioSala;
    private int idSala;
    private int idUsuario;
    private String comentario;
    private int calificacion;
    private LocalDateTime fechaComentario;
    private String estado;

    public ComentarioSala(int idComentarioSala, int idSala, int idUsuario, String comentario, int calificacion, LocalDateTime fechaComentario, String estado) {
        this.idComentarioSala = idComentarioSala;
        this.idSala = idSala;
        this.idUsuario = idUsuario;
        this.comentario = comentario;
        this.calificacion = calificacion;
         // Asignar fecha actual si no se proporciona
        this.fechaComentario = (fechaComentario != null) ? fechaComentario : LocalDateTime.now();
        // Asignar estado por defecto si no se proporciona
        this.estado = (estado != null && !estado.isEmpty()) ? estado : "ACTIVO";
    }

    public int getIdComentarioSala() {
        return idComentarioSala;
    }

    public void setIdComentarioSala(int idComentarioSala) {
        this.idComentarioSala = idComentarioSala;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public LocalDateTime getFechaComentario() {
        return fechaComentario;
    }

    public void setFechaComentario(LocalDateTime fechaComentario) {
        this.fechaComentario = fechaComentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public boolean isValid() {
    return idSala > 0
            && idUsuario > 0
            && StringUtils.isNotBlank(comentario)
            && calificacion >= 1 && calificacion <= 5
            && fechaComentario != null
            && StringUtils.isNotBlank(estado);
}
}
