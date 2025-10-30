package dtos.anuncios;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NewAnuncioRequest {
    private int idUsuario;
    private int idTipoAnuncio;
    private int idPeriodo;
    private String titulo;
    private String contenidoTexto;
    //private byte[] imagenUrl;
    private String videoUrl;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private BigDecimal costoTotal;
    private String estado;
    
    // Constructores, getters y setters
    public NewAnuncioRequest() {}
    
    public NewAnuncioRequest(int idUsuario, int idTipoAnuncio, int idPeriodo, String titulo, 
                           String contenidoTexto, String videoUrl,
                           LocalDateTime fechaInicio, LocalDateTime fechaFin, 
                           BigDecimal costoTotal, String estado) {
        this.idUsuario = idUsuario;
        this.idTipoAnuncio = idTipoAnuncio;
        this.idPeriodo = idPeriodo;
        this.titulo = titulo;
        this.contenidoTexto = contenidoTexto;
        this.videoUrl = videoUrl;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costoTotal = costoTotal;
        this.estado = estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTipoAnuncio() {
        return idTipoAnuncio;
    }

    public void setIdTipoAnuncio(int idTipoAnuncio) {
        this.idTipoAnuncio = idTipoAnuncio;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenidoTexto() {
        return contenidoTexto;
    }

    public void setContenidoTexto(String contenidoTexto) {
        this.contenidoTexto = contenidoTexto;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
}