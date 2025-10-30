/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.anuncios;

import exceptions.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import models.anuncios.TipoAnuncio;
import db.TipoAnuncioDB;

/**
 *
 * @author andy
 */
public class TiposAnuncioService {
    
    public List<TipoAnuncio> getAllTiposAnuncio(){
        TipoAnuncioDB tipoAnuncioDB = new TipoAnuncioDB();
        
        return tipoAnuncioDB.getAllTiposAnuncio();
    }
    
    public TipoAnuncio getTipoAnuncioById(int idTipoAnuncio) throws EntityNotFoundException {
        TipoAnuncioDB tipoAnuncioDB = new TipoAnuncioDB();
        Optional<TipoAnuncio> userOpt = tipoAnuncioDB.getTipoAnuncioById(idTipoAnuncio);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("El Tipo de anuncio con id %s no existe", idTipoAnuncio)
            );
        }
          return userOpt.get();
    }
}
