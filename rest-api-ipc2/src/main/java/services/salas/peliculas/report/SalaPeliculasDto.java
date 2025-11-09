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
public class SalaPeliculasDto {
    private String nombreSala;
    private List<PeliculaProyectadaDto> peliculas;
    
    public SalaPeliculasDto(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public List<PeliculaProyectadaDto> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(List<PeliculaProyectadaDto> peliculas) {
        this.peliculas = peliculas;
    }
    
    
}
