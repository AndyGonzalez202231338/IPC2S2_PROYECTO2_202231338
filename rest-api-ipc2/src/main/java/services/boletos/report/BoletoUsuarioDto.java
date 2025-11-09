/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.boletos.report;

import java.time.LocalDateTime;

/**
 *
 * @author andy
 */
public class BoletoUsuarioDto {
    private String nombreUsuario;
    private Integer cantidadBoletos;
    private Double precioPagado;
    private LocalDateTime fechaCompra;
    
    public BoletoUsuarioDto(String nombreUsuario, Integer cantidadBoletos, Double precioPagado, LocalDateTime fechaCompra) {
        this.nombreUsuario = nombreUsuario;
        this.cantidadBoletos = cantidadBoletos;
        this.precioPagado = precioPagado;
        this.fechaCompra = fechaCompra;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Integer getCantidadBoletos() {
        return cantidadBoletos;
    }

    public void setCantidadBoletos(Integer cantidadBoletos) {
        this.cantidadBoletos = cantidadBoletos;
    }

    public Double getPrecioPagado() {
        return precioPagado;
    }

    public void setPrecioPagado(Double precioPagado) {
        this.precioPagado = precioPagado;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
    
    
}
