package services.cines;

import db.CinesDB;
import dtos.cines.NewCineRequest;
import exceptions.EntityAlreadyExistsException;
import exceptions.UserDataInvalidException;
import models.cines.Cine;

public class CinesCreator {
    public Cine createCine(NewCineRequest newCineRequest) throws UserDataInvalidException,
            EntityAlreadyExistsException {
        CinesDB cinesDB = new CinesDB();
        
        // Verificar si el cine ya existe
        if (cinesDB.existsCine(newCineRequest.getNombre())) {
            throw new EntityAlreadyExistsException(
                    String.format("El cine con nombre %s ya existe", newCineRequest.getNombre()));
        }
        
        // Extraer y validar el cine
        Cine cine = extractCine(newCineRequest);
        
        // Crear el cine en la base de datos
        cinesDB.createCine(cine);
        
        return cine;
    }
    
    private Cine extractCine(NewCineRequest newCineRequest) throws UserDataInvalidException {
        try {
            // Crear cine con la lista de administradores
            Cine cine = new Cine(
                    newCineRequest.getNombre(),
                    newCineRequest.getDireccion(), 
                    newCineRequest.getAdministradores()
            );
            
            // Validar el cine
            if (!cine.isValid()) {
                throw new UserDataInvalidException("Error en los datos enviados");
            }
            
            return cine;
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new UserDataInvalidException("Error en los datos enviados");
        }
    }
}