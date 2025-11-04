package services.salas;

import db.SalasDB;
import dtos.salas.NewSalaRequest;
import exceptions.EntityAlreadyExistsException;
import models.salas.Sala;


public class SalasCreator {
    
    public Sala createSala(NewSalaRequest newSalaRequest) throws EntityAlreadyExistsException {
        SalasDB salasDB = new SalasDB();
        Sala sala = extractSala(newSalaRequest);
        
        // Verificar si ya existe una sala con el mismo nombre en el mismo cine
        if (salasDB.existsSalaByNombreAndCine(newSalaRequest.getNombreSala(), newSalaRequest.getIdCine())) {
            throw new EntityAlreadyExistsException(
                String.format("Ya existe una sala con el nombre '%s' en este cine", 
                    newSalaRequest.getNombreSala()));
        }
        
        // Crear la sala
        salasDB.createSala(sala);
        
        return sala;
    }
    
    private Sala extractSala(NewSalaRequest newSalaRequest) {
        try {
            Sala sala = new Sala(
                newSalaRequest.getIdCine(),
                newSalaRequest.getNombreSala(),
                newSalaRequest.getFilas(),
                newSalaRequest.getColumnas(),
                newSalaRequest.getPermiteComentario(),
                newSalaRequest.getEstado()
            );
            
            // Validaciones básicas
            if (!sala.isValid()) {
                throw new IllegalArgumentException("Error en los datos de la sala");
            }
            
            return sala;
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Error en los datos enviados para la sala: " + e.getMessage());
        }
    }
}
