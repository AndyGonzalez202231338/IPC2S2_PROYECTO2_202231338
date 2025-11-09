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
public class ReporteSalasGustadasDto {
     private List<SalaCalificacionesDto> salas;
    private Double promedioGeneral;
    private Integer totalSalas;
    private Integer totalCalificaciones;
    
    public ReporteSalasGustadasDto(List<SalaCalificacionesDto> salas) {
        this.salas = salas;
        calcularTotales();
    }
    
    private void calcularTotales() {
        this.totalSalas = salas.size();
        this.totalCalificaciones = salas.stream()
            .mapToInt(SalaCalificacionesDto::getTotalCalificaciones)
            .sum();
        this.promedioGeneral = salas.stream()
            .mapToDouble(sala -> sala.getPromedioCalificacion() * sala.getTotalCalificaciones())
            .sum() / (totalCalificaciones > 0 ? totalCalificaciones : 1);
    }

    public List<SalaCalificacionesDto> getSalas() {
        return salas;
    }

    public void setSalas(List<SalaCalificacionesDto> salas) {
        this.salas = salas;
    }

    public Double getPromedioGeneral() {
        return promedioGeneral;
    }

    public void setPromedioGeneral(Double promedioGeneral) {
        this.promedioGeneral = promedioGeneral;
    }

    public Integer getTotalSalas() {
        return totalSalas;
    }

    public void setTotalSalas(Integer totalSalas) {
        this.totalSalas = totalSalas;
    }

    public Integer getTotalCalificaciones() {
        return totalCalificaciones;
    }

    public void setTotalCalificaciones(Integer totalCalificaciones) {
        this.totalCalificaciones = totalCalificaciones;
    }
    
    
}
