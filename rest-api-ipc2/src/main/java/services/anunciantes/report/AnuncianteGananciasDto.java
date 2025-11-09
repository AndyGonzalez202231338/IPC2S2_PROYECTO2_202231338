/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.anunciantes.report;

import java.util.List;

/**
 *
 * @author andy
 */
public class AnuncianteGananciasDto {
    private String nombreAnunciante;
    private Double totalGastado;
    private List<AnuncioDto> anuncios;
    
    public AnuncianteGananciasDto(String nombreAnunciante) {
        this.nombreAnunciante = nombreAnunciante;
        this.totalGastado = 0.0;
    }

    public String getNombreAnunciante() {
        return nombreAnunciante;
    }

    public void setNombreAnunciante(String nombreAnunciante) {
        this.nombreAnunciante = nombreAnunciante;
    }

    public Double getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(Double totalGastado) {
        this.totalGastado = totalGastado;
    }

    public List<AnuncioDto> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(List<AnuncioDto> anuncios) {
        this.anuncios = anuncios;
    }
    
    
}