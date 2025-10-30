/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.anuncios;

import db.PeriodoAnuncioDB;
import exceptions.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import models.anuncios.PeriodoAnuncio;

/**
 *
 * @author andy
 */
public class PeriodosAnuncioService {
    public List<PeriodoAnuncio> getAllPeriodoAnuncio(){
        PeriodoAnuncioDB periodoAnuncioDB = new PeriodoAnuncioDB();
        
        return periodoAnuncioDB.getAllPeriodoAnuncio();
    }
    
    public PeriodoAnuncio getPeriodoAnuncioById(int idPeriodoAnuncio) throws EntityNotFoundException {
        PeriodoAnuncioDB periodoAnuncioDB = new PeriodoAnuncioDB();
        Optional<PeriodoAnuncio> userOpt = periodoAnuncioDB.getPeriodoAnuncioById(idPeriodoAnuncio);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("El periodo de anuncio con id %s no existe", idPeriodoAnuncio)
            );
        }
          return userOpt.get();
    }
}
