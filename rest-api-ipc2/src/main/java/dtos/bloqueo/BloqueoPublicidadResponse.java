package dtos.bloqueo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import models.bloqueo.BloqueoPublicidad;

public class BloqueoPublicidadResponse {
    private int idBloqueoPublicidad;
    private int idCine;
    private int idPublicidad;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal costoTotal;
    private LocalDateTime fechaPago;

    public BloqueoPublicidadResponse(BloqueoPublicidad bloqueo) {
        this.idBloqueoPublicidad = bloqueo.getIdBloqueoPublicidad();
        this.idCine = bloqueo.getIdCine();
        this.idPublicidad = bloqueo.getIdPublicidad();
        this.fechaInicio = bloqueo.getFechaInicio();
        this.fechaFin = bloqueo.getFechaFin();
        this.costoTotal = bloqueo.getCostoTotal();
        this.fechaPago = bloqueo.getFechaPago();
    }

    public int getIdBloqueoPublicidad() {
        return idBloqueoPublicidad;
    }

    public void setIdBloqueoPublicidad(int idBloqueoPublicidad) {
        this.idBloqueoPublicidad = idBloqueoPublicidad;
    }

    public int getIdCine() {
        return idCine;
    }

    public void setIdCine(int idCine) {
        this.idCine = idCine;
    }

    public int getIdPublicidad() {
        return idPublicidad;
    }

    public void setIdPublicidad(int idPublicidad) {
        this.idPublicidad = idPublicidad;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    
}