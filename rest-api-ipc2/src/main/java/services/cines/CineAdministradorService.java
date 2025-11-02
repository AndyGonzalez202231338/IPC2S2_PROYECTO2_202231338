package services.cines;

import db.CineAdministradorDB;
import exceptions.EntityNotFoundException;
import models.cines.Cine;
import java.util.List;

public class CineAdministradorService {
    
    private final CineAdministradorDB cineAdministradorDB;
    
    public CineAdministradorService() {
        this.cineAdministradorDB = new CineAdministradorDB();
    }
    
    public List<Cine> getCinesByAdministrador(int idUsuario) throws EntityNotFoundException {
        List<Cine> cines = cineAdministradorDB.getCinesByUsuarioId(idUsuario);
        
        if (cines.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron cines para el usuario administrador con ID: " + idUsuario);
        }
        
        return cines;
    }
}