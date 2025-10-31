package services.Movies;

import db.MovieDB;
import db.PeliculaCategoriasDB;
import db.MovieDB;
import db.PeliculaCategoriasDB;
import dtos.movies.UpdateMovieRequest;

import exceptions.EntityNotFoundException;
import exceptions.MovieDataInvalidException;
import java.util.List;
import java.util.Optional;
import models.movies.CategoriaPelicula;
import models.movies.Movies;

public class MoviesCrudService {

    private final MovieDB movieDB;
    private final PeliculaCategoriasDB peliculaCategoriasDB;

    public MoviesCrudService() {
        this.movieDB = new MovieDB();
        this.peliculaCategoriasDB = new PeliculaCategoriasDB();
    }

    public List<Movies> getAllMovies() {
        return movieDB.getAllMovies();
    }

    public Movies getMovieById(int id) throws EntityNotFoundException {
        Optional<Movies> movie = movieDB.getMovieById(id);
        if (movie.isEmpty()) {
            throw new EntityNotFoundException("Película no encontrada con ID: " + id);
        }
        return movie.get();
    }

    public Movies updateMovieEstado(int id, String nuevoEstado) throws EntityNotFoundException {
        // Verificar que la película existe
        Optional<Movies> existingMovie = movieDB.getMovieById(id);
        if (existingMovie.isEmpty()) {
            throw new EntityNotFoundException("Película no encontrada con ID: " + id);
        }

        // Validar que el estado sea válido
        if (!nuevoEstado.equals("ACTIVA") && !nuevoEstado.equals("INACTIVA")) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado);
        }

        // Actualizar solo el estado
        boolean success = movieDB.updateMovieEstado(id, nuevoEstado);
        if (!success) {
            throw new RuntimeException("Error al actualizar el estado de la película");
        }

        // Retornar la película actualizada
        Movies updatedMovie = existingMovie.get();
        updatedMovie.setEstado(nuevoEstado);
        return updatedMovie;
    }

    public Movies updateMovie(int id, UpdateMovieRequest movieRequest)
            throws EntityNotFoundException, MovieDataInvalidException {

        // Verificar que la película existe
//        Optional<Movies> existingMovie = movieDB.getMovieById(id);
//        if (existingMovie.isEmpty()) {
//            throw new EntityNotFoundException("Película no encontrada con ID: " + id);
//        }
//        
//        // Validar datos
//        validateUpdateMovieData(movieRequest);
//        
//        // Actualizar la película
//        Movies updatedMovie = new Movies(
//            id,
//            movieRequest.getTitulo(),
//            movieRequest.getSinopsis(),
//            movieRequest.getDuracionMinutos(),
//            movieRequest.getDirector(),
//            movieRequest.getReparto(),
//            movieRequest.getClasificacion(),
//            movieRequest.getFechaEstreno(),
//            existingMovie.get().getPosterUrl(), // Mantener el poster existente
//            movieRequest.getEstado()
//        );
//        
//        boolean success = movieDB.updateMovie(updatedMovie);
//        if (!success) {
//            throw new MovieDataInvalidException("Error al actualizar la película");
//        }
//        
//        return updatedMovie;
        return null;
    }

    public void deleteMovie(int id) throws EntityNotFoundException {
        // Verificar que la película existe
        Optional<Movies> existingMovie = movieDB.getMovieById(id);
        if (existingMovie.isEmpty()) {
            throw new EntityNotFoundException("Película no encontrada con ID: " + id);
        }

        // Primero eliminar las relaciones en pelicula_categorias
        peliculaCategoriasDB.deleteCategoriasByPeliculaId(id);

        // Luego eliminar la película
        boolean success = movieDB.deleteMovie(id);
        if (!success) {
            throw new EntityNotFoundException("Error al eliminar la película");
        }
    }

    public List<CategoriaPelicula> getCategoriasByMovieId(int idPelicula) throws EntityNotFoundException {
        Optional<Movies> existingMovie = movieDB.getMovieById(idPelicula);
        if (existingMovie.isEmpty()) {
            throw new EntityNotFoundException("Película no encontrada con ID: " + idPelicula);
        }

        return peliculaCategoriasDB.getCategoriasByPeliculaId(idPelicula);
    }

    private void validateUpdateMovieData(UpdateMovieRequest movieRequest) throws MovieDataInvalidException {
//        if (movieRequest.getTitulo() == null || movieRequest.getTitulo().trim().isEmpty()) {
//            throw new MovieDataInvalidException("El título es requerido");
//        }
//        if (movieRequest.getDuracionMinutos() <= 0) {
//            throw new MovieDataInvalidException("La duración debe ser mayor a 0");
//        }
//        if (movieRequest.getClasificacion() == null || movieRequest.getClasificacion().trim().isEmpty()) {
//            throw new MovieDataInvalidException("La clasificación es requerida");
//        }
//        if (movieRequest.getEstado() == null || movieRequest.getEstado().trim().isEmpty()) {
//            throw new MovieDataInvalidException("El estado es requerido");
//        }
    }

}
