/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.boletos.report;

import java.util.List;

/**
 *
 * @author andy
 */
public class ReporteBoletosVendidosDto {
    private List<SalaBoletosDto> salas;
    private Double totalGeneral;
    private Integer totalSalas;
    private Integer totalBoletosVendidos;
    
    public ReporteBoletosVendidosDto(List<SalaBoletosDto> salas) {
        this.salas = salas;
        calcularTotales();
    }
    
    private void calcularTotales() {
        this.totalSalas = salas.size();
        this.totalBoletosVendidos = salas.stream()
            .mapToInt(SalaBoletosDto::getTotalBoletos)
            .sum();
        this.totalGeneral = salas.stream()
            .mapToDouble(SalaBoletosDto::getTotalRecaudado)
            .sum();
    }

    public List<SalaBoletosDto> getSalas() {
        return salas;
    }

    public void setSalas(List<SalaBoletosDto> salas) {
        this.salas = salas;
    }

    public Double getTotalGeneral() {
        return totalGeneral;
    }

    public void setTotalGeneral(Double totalGeneral) {
        this.totalGeneral = totalGeneral;
    }

    public Integer getTotalSalas() {
        return totalSalas;
    }

    public void setTotalSalas(Integer totalSalas) {
        this.totalSalas = totalSalas;
    }

    public Integer getTotalBoletosVendidos() {
        return totalBoletosVendidos;
    }

    public void setTotalBoletosVendidos(Integer totalBoletosVendidos) {
        this.totalBoletosVendidos = totalBoletosVendidos;
    }
    
    
    
    
}
