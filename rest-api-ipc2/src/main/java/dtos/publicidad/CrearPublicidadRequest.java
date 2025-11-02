package dtos.publicidad;

import java.math.BigDecimal;

public class CrearPublicidadRequest {
    private int idAnuncio;
    private int idUsuario;
    private BigDecimal precioBloqueo;

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
   
    
}