package services.anuncios.report;

import java.time.LocalDateTime;

public class AnuncioReporteDto {
    private Integer idAnuncio;
    private String titulo;
    private String tipoAnuncio;
    private String periodo;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Double costoTotal;
    private String estado;
    private String usuario;
    
    public AnuncioReporteDto(Integer idAnuncio, String titulo, String tipoAnuncio, String periodo, 
                           LocalDateTime fechaInicio, LocalDateTime fechaFin, Double costoTotal, 
                           String estado, String usuario) {
        this.idAnuncio = idAnuncio;
        this.titulo = titulo;
        this.tipoAnuncio = tipoAnuncio;
        this.periodo = periodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costoTotal = costoTotal;
        this.estado = estado;
        this.usuario = usuario;
    }

    public Integer getIdAnuncio() {
        return idAnuncio;
    }

    public void setIdAnuncio(Integer idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipoAnuncio() {
        return tipoAnuncio;
    }

    public void setTipoAnuncio(String tipoAnuncio) {
        this.tipoAnuncio = tipoAnuncio;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(Double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
}