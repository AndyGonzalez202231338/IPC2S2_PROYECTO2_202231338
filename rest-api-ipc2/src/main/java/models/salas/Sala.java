package models.salas;

public class Sala {
    private int idSala;
    private int idCine;
    private String nombreSala;
    private int filas;
    private int columnas;
    private String permiteComentario;
    private String estado;

    public Sala(int idCine, String nombreSala, int filas, int columnas, 
                String permiteComentario, String estado) {
        this.idCine = idCine;
        this.nombreSala = nombreSala;
        this.filas = filas;
        this.columnas = columnas;
        this.permiteComentario = permiteComentario;
        this.estado = estado;
    }

    public Sala(int idSala, int idCine, String nombreSala, int filas, int columnas, 
                String permiteComentario, String estado) {
        this.idSala = idSala;
        this.idCine = idCine;
        this.nombreSala = nombreSala;
        this.filas = filas;
        this.columnas = columnas;
        this.permiteComentario = permiteComentario;
        this.estado = estado;
    }

    // Método de validación
    public boolean isValid() {
        return idCine > 0 &&
               nombreSala != null && !nombreSala.trim().isEmpty() &&
               filas > 0 && columnas > 0 &&
               permiteComentario != null && (permiteComentario.equals("SI") || permiteComentario.equals("NO")) &&
               estado != null && (estado.equals("ACTIVA") || estado.equals("BLOQUEADA"));
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public int getIdCine() {
        return idCine;
    }

    public void setIdCine(int idCine) {
        this.idCine = idCine;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public String getPermiteComentario() {
        return permiteComentario;
    }

    public void setPermiteComentario(String permiteComentario) {
        this.permiteComentario = permiteComentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
}
