/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author andy
 */
public class Boleto {
    private int idBoleto;
    private int idFuncion;
    private int idUsuario;
    private String codigoBoleto;
    private LocalDateTime fechaCompra;
    private double precioPagado;

    public Boleto(int idBoleto, int idFuncion, int idUsuario, String codigoBoleto, LocalDateTime fechaCompra, double precioPagado) {
        this.idBoleto = idBoleto;
        this.idFuncion = idFuncion;
        this.idUsuario = idUsuario;
        this.codigoBoleto = codigoBoleto;
        this.fechaCompra = fechaCompra;
        this.precioPagado = precioPagado;
    }

    public int getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(int idBoleto) {
        this.idBoleto = idBoleto;
    }

    public int getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(int idFuncion) {
        this.idFuncion = idFuncion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCodigoBoleto() {
        return codigoBoleto;
    }

    public void setCodigoBoleto(String codigoBoleto) {
        this.codigoBoleto = codigoBoleto;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public double getPrecioPagado() {
        return precioPagado;
    }

    public void setPrecioPagado(double precioPagado) {
        this.precioPagado = precioPagado;
    }
    
    public boolean isValid() {
        return idFuncion > 0
                && idUsuario > 0
                && StringUtils.isNotBlank(codigoBoleto)
                && fechaCompra != null
                && precioPagado > 0;
    }
    
    
}
