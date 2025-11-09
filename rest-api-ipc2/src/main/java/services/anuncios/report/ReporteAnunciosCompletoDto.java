package services.anuncios.report;

import java.util.List;

public class ReporteAnunciosCompletoDto {
    private List<AnuncioReporteDto> anuncios;
    private Double totalCosto;
    private Integer totalAnuncios;
    
    public ReporteAnunciosCompletoDto(List<AnuncioReporteDto> anuncios) {
        this.anuncios = anuncios;
        calcularTotales();
    }
    
    private void calcularTotales() {
        this.totalCosto = anuncios.stream()
            .mapToDouble(AnuncioReporteDto::getCostoTotal)
            .sum();
        this.totalAnuncios = anuncios.size();
    }
    
    // Getters

    public List<AnuncioReporteDto> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(List<AnuncioReporteDto> anuncios) {
        this.anuncios = anuncios;
    }

    public Double getTotalCosto() {
        return totalCosto;
    }

    public void setTotalCosto(Double totalCosto) {
        this.totalCosto = totalCosto;
    }

    public Integer getTotalAnuncios() {
        return totalAnuncios;
    }

    public void setTotalAnuncios(Integer totalAnuncios) {
        this.totalAnuncios = totalAnuncios;
    }
    
}