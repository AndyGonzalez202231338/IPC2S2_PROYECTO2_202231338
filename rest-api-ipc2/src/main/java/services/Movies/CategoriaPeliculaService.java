/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.Movies;

import db.CategoriaPeliculaDB;
import exceptions.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import models.movies.CategoriaPelicula;

/**
 *
 * @author andy
 */
public class CategoriaPeliculaService {
    
    public List<CategoriaPelicula> getAllCategoriasPelicula(){
        CategoriaPeliculaDB categoriaPeliculaDB = new CategoriaPeliculaDB();
        
        return categoriaPeliculaDB.getAllCategorias();
    }
    
    public CategoriaPelicula getCategoriaPeliculaById(int idTipoAnuncio) throws EntityNotFoundException {
        CategoriaPeliculaDB categoriaPeliculaDB = new CategoriaPeliculaDB();
        Optional<CategoriaPelicula> userOpt = categoriaPeliculaDB.getCategoriaById(idTipoAnuncio);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("La categoria de pelicula con id %s no existe", idTipoAnuncio)
            );
        }
          return userOpt.get();
    }
    
}
