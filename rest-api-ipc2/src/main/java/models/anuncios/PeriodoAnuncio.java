/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.anuncios;

import java.math.BigDecimal;

/**
 *
 * @author andy
 */
public class PeriodoAnuncio {
    private int idPeriodo;
    private String nombre;
    private int diasDuracion;
    private BigDecimal precioDuracion;

    public PeriodoAnuncio(int idPeriodo, String nombre, int diasDuracion, BigDecimal precioDuracion) {
        this.idPeriodo = idPeriodo;
        this.nombre = nombre;
        this.diasDuracion = diasDuracion;
        this.precioDuracion = precioDuracion;
    }

    public PeriodoAnuncio() {
    }
    
    

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDiasDuracion() {
        return diasDuracion;
    }

    public void setDiasDuracion(int diasDuracion) {
        this.diasDuracion = diasDuracion;
    }

    public BigDecimal getPrecioDuracion() {
        return precioDuracion;
    }

    public void setPrecioDuracion(BigDecimal precioDuracion) {
        this.precioDuracion = precioDuracion;
    }
    
    
}
