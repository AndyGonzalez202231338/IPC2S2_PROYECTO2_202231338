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
public class TipoAnuncio {
    private int idTipoAnuncio;
    private String nombre;
    private String descripcion;
    private BigDecimal precioAnuncio;

    public TipoAnuncio(int idTipoAnuncio, String nombre, String descripcion, BigDecimal precioAnuncio) {
        this.idTipoAnuncio = idTipoAnuncio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioAnuncio = precioAnuncio;
    }

    public TipoAnuncio() {
    }
    
    

    public int getIdTipoAnuncio() {
        return idTipoAnuncio;
    }

    public void setIdTipoAnuncio(int idTipoAnuncio) {
        this.idTipoAnuncio = idTipoAnuncio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioAnuncio() {
        return precioAnuncio;
    }

    public void setPrecioAnuncio(BigDecimal precioAnuncio) {
        this.precioAnuncio = precioAnuncio;
    }
    
    
    
}
