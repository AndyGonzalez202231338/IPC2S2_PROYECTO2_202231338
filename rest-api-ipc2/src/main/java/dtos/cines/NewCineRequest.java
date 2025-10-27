package dtos.cines;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author andy
 */
public class NewCineRequest {
    private String nombre;
    private String direccion;
    private List<Integer> administradores; // IDs de usuarios

    // Constructores
    public NewCineRequest() {}
    
    public NewCineRequest(String nombre, String direccion, List<Integer> administradores) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.administradores = administradores;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public List<Integer> getAdministradores() { return administradores; }
    public void setAdministradores(List<Integer> administradores) { this.administradores = administradores; }
}