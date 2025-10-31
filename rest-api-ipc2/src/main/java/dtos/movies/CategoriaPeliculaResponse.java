/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos.movies;

import models.movies.CategoriaPelicula;

/**
 *
 * @author andy
 */
public class CategoriaPeliculaResponse {
    private int idCategoria;
    private String nombreCategoria;
    private String descripcion;

    public CategoriaPeliculaResponse(CategoriaPelicula categoriaPelicula) {
        this.idCategoria = categoriaPelicula.getIdCategoria();
        this.nombreCategoria = categoriaPelicula.getNombreCategoria();
        this.descripcion = categoriaPelicula.getDescripcion();
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
