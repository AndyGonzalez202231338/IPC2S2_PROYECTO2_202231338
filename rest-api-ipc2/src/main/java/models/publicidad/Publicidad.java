package models.publicidad;

import java.math.BigDecimal;

/**
 *
 * @author andy
 */
public class Publicidad {
    private int idPublicidad;
    private int idAnuncio;
    private int idUsuario;
    private BigDecimal precioBloqueo;
    private String estado;

    public Publicidad() {}

    public Publicidad(int idPublicidad, int idAnuncio, int idUsuario, BigDecimal precioBloqueo, String estado) {
        this.idPublicidad = idPublicidad;
        this.idAnuncio = idAnuncio;
        this.idUsuario = idUsuario;
        this.precioBloqueo = precioBloqueo;
        this.estado = estado;
    }

    public int getIdPublicidad() {
        return idPublicidad;
    }

    public void setIdPublicidad(int idPublicidad) {
        this.idPublicidad = idPublicidad;
    }

    public int getIdAnuncio() {
        return idAnuncio;
    }

    public void setIdAnuncio(int idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigDecimal getPrecioBloqueo() {
        return precioBloqueo;
    }

    public void setPrecioBloqueo(BigDecimal precioBloqueo) {
        this.precioBloqueo = precioBloqueo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
}