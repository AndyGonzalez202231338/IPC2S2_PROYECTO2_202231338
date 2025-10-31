package services.Movies;

import db.MovieDB;
import db.PeliculaCategoriasDB;
import dtos.movies.NewMovieRequest;
import exceptions.EntityAlreadyExistsException;
import exceptions.MovieDataInvalidException;
import java.util.List;
import models.movies.Movies;

public class MoviesCreator {
    
    private final MovieDB movieDB;
    private final PeliculaCategoriasDB peliculaCategoriasDB;
    
    public MoviesCreator() {
        this.movieDB = new MovieDB();
        this.peliculaCategoriasDB = new PeliculaCategoriasDB();
    }
    
    public void createMovieWithCategories(NewMovieRequest movieRequest, List<Integer> categorias) 
            throws MovieDataInvalidException, EntityAlreadyExistsException {
        
        // Validar datos de la película
        validateMovieData(movieRequest);
        
        // Validar que existan las categorías
        validateCategorias(categorias);
        
        try {
            // 1. Crear la película y obtener el ID generado
            Movies movie = new Movies(
                0, // id se generará automáticamente
                movieRequest.getTitulo(),
                movieRequest.getSinopsis(),
                movieRequest.getDuracionMinutos(),
                movieRequest.getDirector(),
                movieRequest.getReparto(),
                movieRequest.getClasificacion(),
                movieRequest.getFechaEstreno(),
                movieRequest.getPosterUrl(),
                movieRequest.getEstado()
            );
            
            int idPeliculaGenerado = movieDB.insertMovie(movie);
            
            // 2. Insertar las relaciones en pelicula_categorias
            for (Integer idCategoria : categorias) {
                peliculaCategoriasDB.insertPeliculaCategoria(idPeliculaGenerado, idCategoria);
            }
            
        } catch (Exception e) {
            throw new MovieDataInvalidException("Error al crear la película: " + e.getMessage());
        }
    }
    
    private void validateMovieData(NewMovieRequest movieRequest) throws MovieDataInvalidException {
        if (movieRequest.getTitulo() == null || movieRequest.getTitulo().trim().isEmpty()) {
            throw new MovieDataInvalidException("El título es requerido");
        }
        if (movieRequest.getDuracionMinutos() <= 0) {
            throw new MovieDataInvalidException("La duración debe ser mayor a 0");
        }
        if (movieRequest.getClasificacion() == null || movieRequest.getClasificacion().trim().isEmpty()) {
            throw new MovieDataInvalidException("La clasificación es requerida");
        }
        if (movieRequest.getEstado() == null || movieRequest.getEstado().trim().isEmpty()) {
            throw new MovieDataInvalidException("El estado es requerido");
        }
    }
    
    private void validateCategorias(List<Integer> categorias) throws MovieDataInvalidException {
        if (categorias == null || categorias.isEmpty()) {
            throw new MovieDataInvalidException("Debe seleccionar al menos una categoría");
        }
        
        // Verificar que todas las categorías existan en la base de datos
        for (Integer idCategoria : categorias) {
            if (!movieDB.categoriaExists(idCategoria)) {
                throw new MovieDataInvalidException("La categoría con ID " + idCategoria + " no existe");
            }
        }
    }
}