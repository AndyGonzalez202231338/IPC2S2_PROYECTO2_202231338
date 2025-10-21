package models.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Role {
    private int idRol;
    private String nombreRol;
    private String descripcion;

    // Constructor por defecto necesario para Jackson
    public Role() {
    }

    // Constructor con parámetros
    @JsonCreator
    public Role(@JsonProperty("idRol") int idRol, 
                @JsonProperty("nombreRol") String nombreRol, 
                @JsonProperty("descripcion") String descripcion) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
    }

    // Getters y setters
    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}