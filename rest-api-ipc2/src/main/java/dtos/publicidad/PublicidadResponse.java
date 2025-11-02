package dtos.publicidad;

import java.math.BigDecimal;
import models.publicidad.Publicidad;

public class PublicidadResponse {
    private int idPublicidad;
    private int idAnuncio;
    private int idUsuario;
    private BigDecimal precioBloqueo;
    private String estado;

    public PublicidadResponse(Publicidad publicidad) {
        this.idPublicidad = publicidad.getIdPublicidad();
        this.idAnuncio = publicidad.getIdAnuncio();
        this.idUsuario = publicidad.getIdUsuario();
        this.precioBloqueo = publicidad.getPrecioBloqueo();
        this.estado = publicidad.getEstado();
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
