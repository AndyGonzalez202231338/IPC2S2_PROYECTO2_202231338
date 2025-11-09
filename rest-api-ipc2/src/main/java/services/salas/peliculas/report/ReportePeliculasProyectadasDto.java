/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.salas.peliculas.report;

import java.util.List;

/**
 *
 * @author andy
 */
public class ReportePeliculasProyectadasDto {
    private List<SalaPeliculasDto> salas;
    private Integer totalSalas;
    private Integer totalPeliculas;
    private String filtroAplicado;
    
    public ReportePeliculasProyectadasDto(List<SalaPeliculasDto> salas, String filtroAplicado) {
        this.salas = salas;
        this.filtroAplicado = filtroAplicado;
        calcularTotales();
    }
    
    private void calcularTotales() {
        this.totalSalas = salas != null ? salas.size() : 0;
        this.totalPeliculas = salas != null ? 
            salas.stream()
                .mapToInt(sala -> sala.getPeliculas() != null ? sala.getPeliculas().size() : 0)
                .sum() : 0;
    }

    public List<SalaPeliculasDto> getSalas() {
        return salas;
    }

    public void setSalas(List<SalaPeliculasDto> salas) {
        this.salas = salas;
    }

    public Integer getTotalSalas() {
        return totalSalas;
    }

    public void setTotalSalas(Integer totalSalas) {
        this.totalSalas = totalSalas;
    }

    public Integer getTotalPeliculas() {
        return totalPeliculas;
    }

    public void setTotalPeliculas(Integer totalPeliculas) {
        this.totalPeliculas = totalPeliculas;
    }

    public String getFiltroAplicado() {
        return filtroAplicado;
    }

    public void setFiltroAplicado(String filtroAplicado) {
        this.filtroAplicado = filtroAplicado;
    }
    
    
}
