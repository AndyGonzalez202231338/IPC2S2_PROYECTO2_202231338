/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos.boletos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import models.Boleto;

/**
 *
 * @author andy
 */
public class BoletoResponse {
    private int idBoleto;
    private int idFuncion;
    private int idUsuario;
    private String codigoBoleto;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm[:ss][.SSS][XXX]")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime fechaCompra;
    private double precioPagado;
    
    public BoletoResponse(Boleto boleto){
       this.idBoleto = boleto.getIdBoleto();
       this.idFuncion = boleto.getIdFuncion();
       this.idUsuario = boleto.getIdUsuario();
       this.codigoBoleto = boleto.getCodigoBoleto();
       this.fechaCompra = boleto.getFechaCompra();
       this.precioPagado = boleto.getPrecioPagado();
        
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
    
    
    
    
    
}
