package dtos.anuncios;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import models.anuncios.Anuncio;
import models.anuncios.PeriodoAnuncio;
import models.anuncios.TipoAnuncio;

public class AnuncioResponse {
    private TipoAnuncioResponse tipoAnuncio;
    private PeriodoAnuncioResponse periodoAnuncio;
    private int idAnuncio;
    private int idUsuario;
    private int idTipoAnuncio;
    private int idPeriodo;
    private String titulo;
    private String contenidoTexto;
    private byte[] imagenUrl;
    private String videoUrl;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaInicio;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaFin;
    private BigDecimal costoTotal;
    private String estado;
    
    public AnuncioResponse(Anuncio anuncio) {
        this.idAnuncio = anuncio.getIdAnuncio();
        this.idUsuario = anuncio.getIdUsuario();
        this.idTipoAnuncio = anuncio.getIdTipoAnuncio();
        this.idPeriodo = anuncio.getIdPeriodo();
        this.titulo = anuncio.getTitulo();
        this.contenidoTexto = anuncio.getContenidoTexto();
        this.imagenUrl = anuncio.getImagenUrl();
        this.videoUrl = anuncio.getVideoUrl();
        this.fechaInicio = anuncio.getFechaInicio();
        this.fechaFin = anuncio.getFechaFin();
        this.costoTotal = anuncio.getCostoTotal();
        this.estado = anuncio.getEstado();
        // Incluir los objetos completos
        if (anuncio.getTipoAnuncio() != null) {
            this.tipoAnuncio = new TipoAnuncioResponse(anuncio.getTipoAnuncio());
        }
        
        if (anuncio.getPeriodoAnuncio() != null) {
            this.periodoAnuncio = new PeriodoAnuncioResponse(anuncio.getPeriodoAnuncio());
        }
    }

    public int getIdAnuncio() {
        return idAnuncio;
    }

    public void setIdAnuncio(int idAnuncio) {
        this.idAnuncio = idAnuncio;
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

    public byte[] getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(byte[] imagenUrl) {
        this.imagenUrl = imagenUrl;
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