package dtos.anuncios;

import java.math.BigDecimal;
import models.anuncios.TipoAnuncio;

public class TipoAnuncioResponse {
    private int idTipoAnuncio;
    private String nombre;
    private String descripcion;
    private BigDecimal precioAnuncio;
    
    public TipoAnuncioResponse(TipoAnuncio tipoAnuncio) {
        this.idTipoAnuncio = tipoAnuncio.getIdTipoAnuncio();
        this.nombre = tipoAnuncio.getNombre();
        this.descripcion = tipoAnuncio.getDescripcion();
        this.precioAnuncio = tipoAnuncio.getPrecioAnuncio();
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
