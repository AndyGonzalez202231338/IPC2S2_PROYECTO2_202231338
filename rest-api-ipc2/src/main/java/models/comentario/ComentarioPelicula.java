package models.comentario;

import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;

public class ComentarioPelicula {
    private int idComentarioPelicula;
    private int idPelicula;
    private int idUsuario;
    private String comentario;
    private int calificacion;
    private LocalDateTime fechaComentario;
    private String estado;

    public ComentarioPelicula(int idComentarioPelicula, int idPelicula, int idUsuario, String comentario, 
                             int calificacion, LocalDateTime fechaComentario, String estado) {
        this.idComentarioPelicula = idComentarioPelicula;
        this.idPelicula = idPelicula;
        this.idUsuario = idUsuario;
        this.comentario = comentario;
        this.calificacion = calificacion;
        // Asignar fecha actual si no se proporciona
        this.fechaComentario = (fechaComentario != null) ? fechaComentario : LocalDateTime.now();
        // Asignar estado por defecto si no se proporciona
        this.estado = (estado != null && !estado.isEmpty()) ? estado : "ACTIVO";
    }

    public int getIdComentarioPelicula() {
        return idComentarioPelicula;
    }

    public void setIdComentarioPelicula(int idComentarioPelicula) {
        this.idComentarioPelicula = idComentarioPelicula;
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
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
        return idPelicula > 0
                && idUsuario > 0
                && StringUtils.isNotBlank(comentario)
                && calificacion >= 1 && calificacion <= 5
                && fechaComentario != null
                && StringUtils.isNotBlank(estado);
    }
}
