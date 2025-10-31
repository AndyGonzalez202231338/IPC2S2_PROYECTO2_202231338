/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos.movies;

import java.util.Date;

/**
 *
 * @author andy
 */
public class NewMovieRequest {
    private int idPelicula;
    private String titulo;
    private String sinopsis;
    private int duracionMinutos;
    private String director;
    private String reparto;
    private String clasificacion;
    private Date fechaEstreno;
    private Byte[] posterUrl;
    private String estado;

    public NewMovieRequest(int idPelicula, String titulo, String sinopsis, int duracionMinutos, String director, String reparto, String clasificacion, Date fechaEstreno, Byte[] posterUrl, String estado) {
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.duracionMinutos = duracionMinutos;
        this.director = director;
        this.reparto = reparto;
        this.clasificacion = clasificacion;
        this.fechaEstreno = fechaEstreno;
        this.posterUrl = posterUrl;
        this.estado = estado;
    }

    public NewMovieRequest() {
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getReparto() {
        return reparto;
    }

    public void setReparto(String reparto) {
        this.reparto = reparto;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public Date getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(Date fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public Byte[] getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(Byte[] posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
    
}
