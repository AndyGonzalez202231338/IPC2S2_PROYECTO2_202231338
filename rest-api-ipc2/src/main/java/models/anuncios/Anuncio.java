/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.anuncios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author andy
 */
public class Anuncio {
    private TipoAnuncio tipoAnuncio;
    private PeriodoAnuncio periodoAnuncio;
    
    private int idAnuncio;
    private int idUsuario;
    private int idTipoAnuncio;
    private int idPeriodo;
    private String titulo;
    private String contenidoTexto;
    private byte[] imagenUrl;
    private String videoUrl;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private BigDecimal costoTotal;
    private String estado;

    public Anuncio(int idAnuncio, int idUsuario, int idTipoAnuncio, int idPeriodo, String titulo, String contenidoTexto, byte[] imagenUrl, String videoUrl, LocalDateTime fechaInicio, LocalDateTime fechaFin, BigDecimal costoTotal, String estado) {
        this.idAnuncio = idAnuncio;
        this.idUsuario = idUsuario;
        this.idTipoAnuncio = idTipoAnuncio;
        this.idPeriodo = idPeriodo;
        this.titulo = titulo;
        this.contenidoTexto = contenidoTexto;
        this.imagenUrl = imagenUrl;
        this.videoUrl = videoUrl;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costoTotal = costoTotal;
        this.estado = estado;
    }

    public Anuncio(int idUsuario, int idTipoAnuncio, int idPeriodo, String titulo, String contenidoTexto, byte[] imagenUrl, String videoUrl, LocalDateTime fechaInicio, LocalDateTime fechaFin, BigDecimal costoTotal, String estado) {
        this.idUsuario = idUsuario;
        this.idTipoAnuncio = idTipoAnuncio;
        this.idPeriodo = idPeriodo;
        this.titulo = titulo;
        this.contenidoTexto = contenidoTexto;
        this.imagenUrl = imagenUrl;
        this.videoUrl = videoUrl;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costoTotal = costoTotal;
        this.estado = estado;
    }

    public Anuncio() {
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
    
    public boolean isValid() {
    return idUsuario > 0
            && idTipoAnuncio > 0
            && idPeriodo > 0
            && StringUtils.isNotBlank(titulo)
            && StringUtils.isNotBlank(contenidoTexto)
            && fechaInicio != null
            && fechaFin != null
            && costoTotal != null
            && costoTotal.compareTo(BigDecimal.ZERO) > 0
            && StringUtils.isNotBlank(estado);
}

    public TipoAnuncio getTipoAnuncio() {
        return tipoAnuncio;
    }

    public void setTipoAnuncio(TipoAnuncio tipoAnuncio) {
        this.tipoAnuncio = tipoAnuncio;
    }

    public PeriodoAnuncio getPeriodoAnuncio() {
        return periodoAnuncio;
    }

    public void setPeriodoAnuncio(PeriodoAnuncio periodoAnuncio) {
        this.periodoAnuncio = periodoAnuncio;
    }
    
    
    
}
