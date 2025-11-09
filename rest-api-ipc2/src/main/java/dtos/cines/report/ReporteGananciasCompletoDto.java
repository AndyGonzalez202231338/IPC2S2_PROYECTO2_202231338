/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos.cines.report;

import java.util.List;

/**
 *
 * @author andy
 */

public class ReporteGananciasCompletoDto {
    private List<ReporteGananciasCineDto> cines;
    private Double totalCostosTodosCines;
    private Double totalIngresosAnuncios;
    private Double totalPorAnuncios;
    private Double totalGanancia;
    
    public ReporteGananciasCompletoDto(List<ReporteGananciasCineDto> cines, 
                                     Double totalPorAnuncios) {
        this.cines = cines;
        this.totalPorAnuncios = totalPorAnuncios != null ? totalPorAnuncios : 0.0;
        calcularTotales();
    }
    
    private void calcularTotales() {
        // Total costos por todos los cines
        this.totalCostosTodosCines = cines.stream()
            .mapToDouble(ReporteGananciasCineDto::getTotalCostoCine)
            .sum();
            
        // Total ingresos por anuncios (bloqueos)
        this.totalIngresosAnuncios = cines.stream()
            .mapToDouble(ReporteGananciasCineDto::getPagoBloqueoAnuncios)
            .sum();
            
        // Total ganancia
        this.totalGanancia = this.totalCostosTodosCines + this.totalIngresosAnuncios + this.totalPorAnuncios;
    }
    
    // Getters
    public List<ReporteGananciasCineDto> getCines() { return cines; }
    public Double getTotalCostosTodosCines() { return totalCostosTodosCines; }
    public Double getTotalIngresosAnuncios() { return totalIngresosAnuncios; }
    public Double getTotalPorAnuncios() { return totalPorAnuncios; }
    public Double getTotalGanancia() { return totalGanancia; }
}
