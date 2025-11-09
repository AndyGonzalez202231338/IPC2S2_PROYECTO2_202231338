/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.salas.calificacion.report;

import java.util.List;

/**
 *
 * @author andy
 */
public class SalaCalificacionesDto {
    private String nombreSala;
    private Double promedioCalificacion;
    private Integer totalCalificaciones;
    private List<CalificacionDto> calificaciones;
    
    public SalaCalificacionesDto(String nombreSala) {
        this.nombreSala = nombreSala;
        this.promedioCalificacion = 0.0;
        this.totalCalificaciones = 0;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public Double getPromedioCalificacion() {
        return promedioCalificacion;
    }

    public void setPromedioCalificacion(Double promedioCalificacion) {
        this.promedioCalificacion = promedioCalificacion;
    }

    public Integer getTotalCalificaciones() {
        return totalCalificaciones;
    }

    public void setTotalCalificaciones(Integer totalCalificaciones) {
        this.totalCalificaciones = totalCalificaciones;
    }

    public List<CalificacionDto> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<CalificacionDto> calificaciones) {
        this.calificaciones = calificaciones;
    }
    
    
}
