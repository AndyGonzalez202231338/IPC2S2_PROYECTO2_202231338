package models.bloqueo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BloqueoPublicidad {
    private Integer idBloqueoPublicidad;
    private Integer idCine;
    private Integer idPublicidad;
    private LocalDate fechaInicio;  
    private LocalDate fechaFin;     
    private BigDecimal costoTotal;
    private LocalDateTime fechaPago; 


    public BloqueoPublicidad(Integer idCine, Integer idPublicidad, 
                            LocalDate fechaInicio, LocalDate fechaFin, 
                            BigDecimal costoTotal) {
        this.idCine = idCine;
        this.idPublicidad = idPublicidad;
        this.fechaInicio = fechaInicio;  // Puede ser null
        this.fechaFin = fechaFin;        // Puede ser null
        this.costoTotal = costoTotal;
        this.fechaPago = (fechaPago != null) ? fechaPago : LocalDateTime.now();

    }


    public boolean isValid() {
        return idCine != null && idCine > 0 &&
               idPublicidad != null && idPublicidad > 0 &&
               costoTotal != null &&
               costoTotal.compareTo(BigDecimal.ZERO) > 0;

    }

    public Integer getIdBloqueoPublicidad() {
        return idBloqueoPublicidad;
    }

    public void setIdBloqueoPublicidad(Integer idBloqueoPublicidad) {
        this.idBloqueoPublicidad = idBloqueoPublicidad;
    }

    public Integer getIdCine() {
        return idCine;
    }

    public void setIdCine(Integer idCine) {
        this.idCine = idCine;
    }

    public Integer getIdPublicidad() {
        return idPublicidad;
    }

    public void setIdPublicidad(Integer idPublicidad) {
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
