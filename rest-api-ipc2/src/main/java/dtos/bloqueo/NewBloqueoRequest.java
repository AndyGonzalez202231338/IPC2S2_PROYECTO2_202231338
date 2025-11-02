package dtos.bloqueo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NewBloqueoRequest {
    private int idAnuncio;
    private int idCine;
    private int idPublicidad;
    private BigDecimal costoTotal;

    public NewBloqueoRequest(int idAnuncio, int idCine, int idPublicidad, BigDecimal costoTotal) {
        this.idAnuncio = idAnuncio;
        this.idCine = idCine;
        this.idPublicidad = idPublicidad;
        this.costoTotal = costoTotal;
    }

    public NewBloqueoRequest() {
    }

    public int getIdAnuncio() {
        return idAnuncio;
    }

    public void setIdAnuncio(int idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    public int getIdCine() {
        return idCine;
    }

    public void setIdCine(int idCine) {
        this.idCine = idCine;
    }

    public int getIdPublicidad() {
        return idPublicidad;
    }

    public void setIdPublicidad(int idPublicidad) {
        this.idPublicidad = idPublicidad;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }
    
    
    
    
    
}
