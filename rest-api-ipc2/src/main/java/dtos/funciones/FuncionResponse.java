/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos.funciones;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import models.funcion.Funcion;

/**
 *
 * @author andy
 */
public class FuncionResponse {
    private int idFuncion;
    private int idSala;
    private int idPelicula;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm[:ss][.SSS][XXX]")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime fechaHoraFuncion;
    private double precioBoletoAdulto;
    private double precioBoletoNino;
    private int asientosDisponibles;
    private String estado;
    
    public FuncionResponse(Funcion funcion){
        this.idFuncion = funcion.getIdFuncion();
        this.idSala = funcion.getIdSala();
        this.idPelicula = funcion.getIdPelicula();
        this.fechaHoraFuncion = funcion.getFechaHoraFuncion();
        this.precioBoletoAdulto = funcion.getPrecioBoletoAdulto();
        this.precioBoletoNino = funcion.getPrecioBoletoAdulto();
        this.asientosDisponibles = funcion.getAsientosDisponibles();
        this.estado = funcion.getEstado();
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
    
    
    
    
}
