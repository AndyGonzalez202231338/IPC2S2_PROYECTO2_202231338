/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.cines;

import db.CinesDB;
import exceptions.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import models.cines.Cine;

/**
 *
 * @author andy
 */
public class CinesCrudServices {
    public List<Cine> getAllCines() {
        CinesDB cinesDB = new CinesDB();

        //return eventsDb.getAllEvents();
        return cinesDB.getAllCines();
    }
    
    public Cine getCineById(int id) throws EntityNotFoundException {
    CinesDB cinesDB = new CinesDB();
    Optional<Cine> cineOpt = cinesDB.getCineById(id);
    if (cineOpt.isEmpty()) {
        throw new EntityNotFoundException(
            String.format("No existe el cine con id %d", id)
        );
    }
    return cineOpt.get();
}
}
