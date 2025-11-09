package services.salas.report;

import java.util.List;

public class SalaPopularDto {
    private String nombreSala;
    private Double promedioCalificacion;
    private Integer totalBoletosVendidos;
    private List<UsuarioBoletosDto> usuarios;
    
    public SalaPopularDto(String nombreSala) {
        this.nombreSala = nombreSala;
        this.promedioCalificacion = 0.0;
        this.totalBoletosVendidos = 0;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public Double getPromedioCalificacion() {
        return promedioCalificacion;
    }

    public void setPromedioCalificacion(Double promedioCalificacion) {
        this.promedioCalificacion = promedioCalificacion;
    }

    public Integer getTotalBoletosVendidos() {
        return totalBoletosVendidos;
    }

    public void setTotalBoletosVendidos(Integer totalBoletosVendidos) {
        this.totalBoletosVendidos = totalBoletosVendidos;
    }
    

    
    public List<UsuarioBoletosDto> getUsuarios() { return usuarios; }
    public void setUsuarios(List<UsuarioBoletosDto> usuarios) { 
        this.usuarios = usuarios; 
    }
}