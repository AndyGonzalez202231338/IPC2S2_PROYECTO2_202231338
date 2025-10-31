package dtos.movies;


import java.util.Date;
import models.movies.Movies;

public class MovieResponse {
    private int idPelicula;
    private String titulo;
    private String sinopsis;
    private int duracionMinutos;
    private String director;
    private String reparto;
    private String clasificacion;
    private Date fechaEstreno;
    private Byte[] posterUrl; // Cambiado a Byte[] para consistencia
    private String estado;
    
    public MovieResponse(Movies movie) {
        this.idPelicula = movie.getIdPelicula();
        this.titulo = movie.getTitulo();
        this.sinopsis = movie.getSinopsis();
        this.duracionMinutos = movie.getDuracionMinutos();
        this.director = movie.getDirector();
        this.reparto = movie.getReparto();
        this.clasificacion = movie.getClasificacion();
        this.fechaEstreno = movie.getFechaEstreno();
        this.posterUrl = movie.getPosterUrl(); // Mantener como Byte[]
        this.estado = movie.getEstado();
    }
    
    // Getters
    public int getIdPelicula() { return idPelicula; }
    public String getTitulo() { return titulo; }
    public String getSinopsis() { return sinopsis; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public String getDirector() { return director; }
    public String getReparto() { return reparto; }
    public String getClasificacion() { return clasificacion; }
    public Date getFechaEstreno() { return fechaEstreno; }
    public Byte[] getPosterUrl() { return posterUrl; }
    public String getEstado() { return estado; }
}
