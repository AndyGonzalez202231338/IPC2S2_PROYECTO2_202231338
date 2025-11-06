// NewComentarioPeliculaRequest.java
package dtos.comentariosPeliculas;

import java.time.LocalDateTime;

public class NewComentarioPeliculaRequest {
    private int idComentarioPelicula;
    private int idPelicula;
    private int idUsuario;
    private String comentario;
    private int calificacion;
    private LocalDateTime fechaComentario;
    private String estado;

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

    
}