/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.funcion;

import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author andy
 */
public class Funcion {
    private int idFuncion;
    private int idSala;
    private int idPelicula;
    private LocalDateTime fechaHoraFuncion;
    private double precioBoletoAdulto;
    private double precioBoletoNino;
    private int asientosDisponibles;
    private String estado;

    public Funcion(int idFuncion, int idSala, int idPelicula, LocalDateTime fechaHoraFuncion, double precioBoletoAdulto, double precioBoletoNino, int asientosDisponibles, String estado) {
        this.idFuncion = idFuncion;
        this.idSala = idSala;
        this.idPelicula = idPelicula;
        this.fechaHoraFuncion = fechaHoraFuncion;
        this.precioBoletoAdulto = precioBoletoAdulto;
        this.precioBoletoNino = precioBoletoNino;
        this.asientosDisponibles = asientosDisponibles;
        this.estado = estado;
    }

    public Funcion() {
    }

    public int getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(int idFuncion) {
        this.idFuncion = idFuncion;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public LocalDateTime getFechaHoraFuncion() {
        return fechaHoraFuncion;
    }

    public void setFechaHoraFuncion(LocalDateTime fechaHoraFuncion) {
        this.fechaHoraFuncion = fechaHoraFuncion;
    }

    public double getPrecioBoletoAdulto() {
        return precioBoletoAdulto;
    }

    public void setPrecioBoletoAdulto(double precioBoletoAdulto) {
        this.precioBoletoAdulto = precioBoletoAdulto;
    }

    public double getPrecioBoletoNino() {
        return precioBoletoNino;
    }

    public void setPrecioBoletoNino(double precioBoletoNino) {
        this.precioBoletoNino = precioBoletoNino;
    }

    public int getAsientosDisponibles() {
        return asientosDisponibles;
    }

    public void setAsientosDisponibles(int asientosDisponibles) {
        this.asientosDisponibles = asientosDisponibles;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public boolean isValid() {
    return 
            fechaHoraFuncion != null;
    }
    
}
