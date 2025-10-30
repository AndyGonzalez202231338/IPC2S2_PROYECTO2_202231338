
package dtos.anuncios;

import java.math.BigDecimal;
import models.anuncios.PeriodoAnuncio;

public class PeriodoAnuncioResponse {
    private int idPeriodo;
    private String nombre;
    private int diasDuracion;
    private BigDecimal precioDuracion;
    
    public PeriodoAnuncioResponse(PeriodoAnuncio periodoAnuncio) {
        this.idPeriodo = periodoAnuncio.getIdPeriodo();
        this.nombre = periodoAnuncio.getNombre();
        this.diasDuracion = periodoAnuncio.getDiasDuracion();
        this.precioDuracion = periodoAnuncio.getPrecioDuracion();
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