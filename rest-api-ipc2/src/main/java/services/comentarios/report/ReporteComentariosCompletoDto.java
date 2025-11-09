/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.comentarios.report;

import java.util.List;

/**
 *
 * @author andy
 */
public class ReporteComentariosCompletoDto {
    private List<ComentarioSalaDto> comentarios;
    private Integer totalComentarios;
    private Double promedioCalificacion;
    
    public ReporteComentariosCompletoDto(List<ComentarioSalaDto> comentarios) {
        this.comentarios = comentarios;
        calcularTotales();
    }
    
    private void calcularTotales() {
        this.totalComentarios = comentarios.size();
        
        if (totalComentarios > 0) {
            this.promedioCalificacion = comentarios.stream()
                .mapToInt(ComentarioSalaDto::getCalificacion)
                .average()
                .orElse(0.0);
        } else {
            this.promedioCalificacion = 0.0;
        }
    }

    public List<ComentarioSalaDto> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<ComentarioSalaDto> comentarios) {
        this.comentarios = comentarios;
    }

    public Integer getTotalComentarios() {
        return totalComentarios;
    }

    public void setTotalComentarios(Integer totalComentarios) {
        this.totalComentarios = totalComentarios;
    }

    public Double getPromedioCalificacion() {
        return promedioCalificacion;
    }

    public void setPromedioCalificacion(Double promedioCalificacion) {
        this.promedioCalificacion = promedioCalificacion;
    }
    
    
}
