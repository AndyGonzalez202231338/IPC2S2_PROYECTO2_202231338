// models/cines/CineAdministrador.java
package models.cines;

import java.time.LocalDateTime;

public class CineAdministrador {
    private Integer idCineAdministrador;
    private Integer idCine;
    private Integer idUsuario;
    private LocalDateTime fechaAsignacion;
    private String estado; // "ACTIVO", "INACTIVO"

    // Constructores
    public CineAdministrador() {}
    
    public CineAdministrador(Integer idCineAdministrador, Integer idCine, Integer idUsuario, 
                           LocalDateTime fechaAsignacion, String estado) {
        this.idCineAdministrador = idCineAdministrador;
        this.idCine = idCine;
        this.idUsuario = idUsuario;
        this.fechaAsignacion = fechaAsignacion;
        this.estado = estado;
    }

    // Getters y Setters
    public Integer getIdCineAdministrador() { return idCineAdministrador; }
    public void setIdCineAdministrador(Integer idCineAdministrador) { this.idCineAdministrador = idCineAdministrador; }
    
    public Integer getIdCine() { return idCine; }
    public void setIdCine(Integer idCine) { this.idCine = idCine; }
    
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    
    public LocalDateTime getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}