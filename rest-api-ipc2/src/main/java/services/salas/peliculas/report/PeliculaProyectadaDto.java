/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.salas.peliculas.report;

import java.time.LocalDateTime;

/**
 *
 * @author andy
 */
public class PeliculaProyectadaDto {
    private String tituloPelicula;
    private LocalDateTime fechaHoraFuncion;
    
    public PeliculaProyectadaDto(String tituloPelicula, LocalDateTime fechaHoraFuncion) {
        this.tituloPelicula = tituloPelicula;
        this.fechaHoraFuncion = fechaHoraFuncion;
    }

    public String getTituloPelicula() {
        return tituloPelicula;
    }

    public void setTituloPelicula(String tituloPelicula) {
        this.tituloPelicula = tituloPelicula;
    }

    public LocalDateTime getFechaHoraFuncion() {
        return fechaHoraFuncion;
    }

    public void setFechaHoraFuncion(LocalDateTime fechaHoraFuncion) {
        this.fechaHoraFuncion = fechaHoraFuncion;
    }
    
    
}
