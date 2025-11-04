package dtos.salas;

import models.salas.Sala;

public class SalaResponse {
    private int idSala;
    private int idCine;
    private String nombreSala;
    private int filas;
    private int columnas;
    private String permiteComentario;
    private String estado;

    public SalaResponse(Sala sala) {
        this.idSala = sala.getIdSala();
        this.idCine = sala.getIdCine();
        this.nombreSala = sala.getNombreSala();
        this.filas = sala.getFilas();
        this.columnas = sala.getColumnas();
        this.permiteComentario = sala.getPermiteComentario();
        this.estado = sala.getEstado();
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