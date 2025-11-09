/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.salasComentarios.report;

import java.util.List;

/**
 *
 * @author andy
 */
public class ReporteComentariosSalasDto {
    private List<ComentarioSalaDto> comentarios;
    private Integer totalComentarios;
    private String filtroAplicado;
    
    public ReporteComentariosSalasDto(List<ComentarioSalaDto> comentarios, String filtroAplicado) {
        this.comentarios = comentarios;
        this.filtroAplicado = filtroAplicado;
        this.totalComentarios = comentarios != null ? comentarios.size() : 0;
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

    public String getFiltroAplicado() {
        return filtroAplicado;
    }

    public void setFiltroAplicado(String filtroAplicado) {
        this.filtroAplicado = filtroAplicado;
    }
    
    
}
