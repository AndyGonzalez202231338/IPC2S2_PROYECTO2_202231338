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
public class ReporteAnunciantesCompletoDto {
    private List<AnuncianteGananciasDto> anunciantes;
    private Double totalGeneral;
    private Integer totalAnunciantes;
    
    public ReporteAnunciantesCompletoDto(List<AnuncianteGananciasDto> anunciantes) {
        this.anunciantes = anunciantes;
        calcularTotales();
    }
    
    private void calcularTotales() {
        this.totalAnunciantes = anunciantes.size();
        this.totalGeneral = anunciantes.stream()
            .mapToDouble(AnuncianteGananciasDto::getTotalGastado)
            .sum();
    }

    public List<AnuncianteGananciasDto> getAnunciantes() {
        return anunciantes;
    }

    public void setAnunciantes(List<AnuncianteGananciasDto> anunciantes) {
        this.anunciantes = anunciantes;
    }

    public Double getTotalGeneral() {
        return totalGeneral;
    }

    public void setTotalGeneral(Double totalGeneral) {
        this.totalGeneral = totalGeneral;
    }

    public Integer getTotalAnunciantes() {
        return totalAnunciantes;
    }

    public void setTotalAnunciantes(Integer totalAnunciantes) {
        this.totalAnunciantes = totalAnunciantes;
    }
    
    
}
