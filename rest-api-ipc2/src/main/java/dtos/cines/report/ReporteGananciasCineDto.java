/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos.cines.report;

/**
 *
 * @author andy
 */

public class ReporteGananciasCineDto {
    private String nombreCine;
    private int idCine;
    private Double costoDiarioTotal;
    private Double pagoBloqueoAnuncios;
    private Double totalCostoCine;
    
    // Constructor actualizado
    public ReporteGananciasCineDto(String nombreCine, int idCine) {
        this.nombreCine = nombreCine;
        this.idCine = idCine;
        this.costoDiarioTotal = 0.0;
        this.pagoBloqueoAnuncios = 0.0;
        this.totalCostoCine = 0.0;
    }
    
    // Getters y Setters
    public String getNombreCine() { return nombreCine; }
    public void setNombreCine(String nombreCine) { this.nombreCine = nombreCine; }
    
    public int getIdCine() { return idCine; }
    public void setIdCine(int idCine) { this.idCine = idCine; }
    
    public Double getCostoDiarioTotal() { return costoDiarioTotal; }
    public void setCostoDiarioTotal(Double costoDiarioTotal) { 
        this.costoDiarioTotal = costoDiarioTotal != null ? costoDiarioTotal : 0.0;
        calcularTotalCostoCine();
    }
    
    public Double getPagoBloqueoAnuncios() { return pagoBloqueoAnuncios; }
    public void setPagoBloqueoAnuncios(Double pagoBloqueoAnuncios) { 
        this.pagoBloqueoAnuncios = pagoBloqueoAnuncios != null ? pagoBloqueoAnuncios : 0.0;
        calcularTotalCostoCine();
    }
    
    public Double getTotalCostoCine() { return totalCostoCine; }
    
    private void calcularTotalCostoCine() {
        this.totalCostoCine = this.costoDiarioTotal + this.pagoBloqueoAnuncios;
    }
}
