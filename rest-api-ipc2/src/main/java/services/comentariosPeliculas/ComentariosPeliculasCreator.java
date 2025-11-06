package services.comentariosPeliculas;

import db.ComentariosPeliculasDB;
import dtos.comentariosPeliculas.NewComentarioPeliculaRequest;
import exceptions.EntityAlreadyExistsException;
import exceptions.UserDataInvalidException;
import models.comentario.ComentarioPelicula;

public class ComentariosPeliculasCreator {
    public ComentarioPelicula createComentarioPelicula(NewComentarioPeliculaRequest NewComentarioPeliculaRequest) 
            throws UserDataInvalidException, EntityAlreadyExistsException {
        
        ComentariosPeliculasDB comentariosPeliculasDB = new ComentariosPeliculasDB();
        ComentarioPelicula comentarioPelicula = extractComentarioPelicula(NewComentarioPeliculaRequest);
        
        // Verificar si ya existe un comentario para este usuario y película
        if (comentariosPeliculasDB.existsComentarioPelicula(
            NewComentarioPeliculaRequest.getIdUsuario(), 
            NewComentarioPeliculaRequest.getIdPelicula())) {
            
            throw new EntityAlreadyExistsException(
                String.format("El usuario con id %s ya tiene un comentario para esta película", 
                comentarioPelicula.getIdUsuario()));
        }
        
        // Crear el comentario
        comentariosPeliculasDB.createComentarioPelicula(comentarioPelicula);
        
        return comentarioPelicula;
    }
    
    private ComentarioPelicula extractComentarioPelicula(NewComentarioPeliculaRequest NewComentarioPeliculaRequest) 
            throws UserDataInvalidException {
        try {
            
            ComentarioPelicula comentarioPelicula = new ComentarioPelicula(
                NewComentarioPeliculaRequest.getIdComentarioPelicula(),
                NewComentarioPeliculaRequest.getIdPelicula(),
                NewComentarioPeliculaRequest.getIdUsuario(),
                NewComentarioPeliculaRequest.getComentario(),
                NewComentarioPeliculaRequest.getCalificacion(),
                NewComentarioPeliculaRequest.getFechaComentario(),
                NewComentarioPeliculaRequest.getEstado()      
            );
            
            if (!comentarioPelicula.isValid()) {
                throw new UserDataInvalidException("Error en los datos enviados");
            }
            
            return comentarioPelicula;
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new UserDataInvalidException("Error en los datos enviados");
        }
    }
}