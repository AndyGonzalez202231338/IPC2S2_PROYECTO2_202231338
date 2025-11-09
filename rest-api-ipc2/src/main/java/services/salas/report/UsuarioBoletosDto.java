package services.salas.report;

public class UsuarioBoletosDto {
    private String nombreUsuario;
    private Integer cantidadBoletos;
    
    public UsuarioBoletosDto(String nombreUsuario, Integer cantidadBoletos) {
        this.nombreUsuario = nombreUsuario;
        this.cantidadBoletos = cantidadBoletos;
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
    
    
}