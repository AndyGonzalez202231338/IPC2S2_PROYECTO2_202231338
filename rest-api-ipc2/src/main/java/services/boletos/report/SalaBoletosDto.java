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
public class SalaBoletosDto {
       private String nombreSala;
    private Double totalRecaudado;
    private Integer totalBoletos;
    private List<BoletoUsuarioDto> boletosUsuarios;
    
    public SalaBoletosDto(String nombreSala) {
        this.nombreSala = nombreSala;
        this.totalRecaudado = 0.0;
        this.totalBoletos = 0;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public Double getTotalRecaudado() {
        return totalRecaudado;
    }

    public void setTotalRecaudado(Double totalRecaudado) {
        this.totalRecaudado = totalRecaudado;
    }

    public Integer getTotalBoletos() {
        return totalBoletos;
    }

    public void setTotalBoletos(Integer totalBoletos) {
        this.totalBoletos = totalBoletos;
    }

    public List<BoletoUsuarioDto> getBoletosUsuarios() {
        return boletosUsuarios;
    }

    public void setBoletosUsuarios(List<BoletoUsuarioDto> boletosUsuarios) {
        this.boletosUsuarios = boletosUsuarios;
    }
    
    
}
