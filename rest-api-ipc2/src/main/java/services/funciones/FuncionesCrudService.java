package services.funciones;

import db.FuncionDB;
import exceptions.EntityNotFoundException;
import exceptions.FuncionDataInvalidException;
import java.util.List;
import java.util.Optional;
import models.funcion.Funcion;

/**
 * Servicio CRUD para las funciones de cine
 * @author andy
 */
public class FuncionesCrudService {
    
    // Obtener todas las funciones registradas
    public List<Funcion> getAllFunciones() {
        FuncionDB funcionesDB = new FuncionDB();
        return funcionesDB.getAllFunciones();
    }

    // Obtener todas las funciones de un cine (JOIN con sala)
    public List<Funcion> getFuncionesByCine(int idCine) {
        FuncionDB funcionesDB = new FuncionDB();
        return funcionesDB.getByCine(idCine);
    }
    
    // Obtener todas las funciones de una pelicula
    public List<Funcion> getFuncionesByMovie(int idMovie) {
        FuncionDB funcionesDB = new FuncionDB();
        return funcionesDB.getByMovie(idMovie);
    }
    
    public List<Funcion> getfuncionesBySala(int idSala) {
        FuncionDB funcionesDB = new FuncionDB();
        return funcionesDB.getBySala(idSala);
    }
    
    

    // Obtener una función específica por su ID
    public Funcion getFuncionById(int idFuncion) throws EntityNotFoundException {
        FuncionDB funcionesDB = new FuncionDB();
        Optional<Funcion> funcionOpt = funcionesDB.getById(idFuncion);
        if (funcionOpt.isEmpty()) {
            throw new EntityNotFoundException(
                String.format("No existe la función con id %d", idFuncion)
            );
        }
        return funcionOpt.get();
    }

    // Crear una nueva función
    public Funcion createFuncion(Funcion nuevaFuncion) throws FuncionDataInvalidException {
        if (!nuevaFuncion.isValid()) {
            throw new FuncionDataInvalidException("Datos inválidos al crear la función");
        }

        FuncionDB funcionesDB = new FuncionDB();
        funcionesDB.createFuncion(nuevaFuncion);
        return nuevaFuncion;
    }

    // Actualizar una función existente
//    public Funcion updateFuncion(int idFuncion, UpdateFuncionRequest updateRequest)
//            throws FuncionDataInvalidException, EntityNotFoundException {
//        
//        FuncionDB funcionesDB = new FuncionDB();
//        Funcion funcion = getFuncionById(idFuncion);
//
//        // Actualizar los campos permitidos
//        funcion.setIdSala(updateRequest.getIdSala());
//        funcion.setIdPelicula(updateRequest.getIdPelicula());
//        funcion.setFechaHoraFuncion(updateRequest.getFechaHoraFuncion());
//        funcion.setPrecioBoletoAdulto(updateRequest.getPrecioBoletoAdulto());
//        funcion.setPrecioBoletoNino(updateRequest.getPrecioBoletoNino());
//        funcion.setAsientosDisponibles(updateRequest.getAsientosDisponibles());
//        funcion.setEstado(updateRequest.getEstado());
//
//        if (!funcion.isValid()) {
//            throw new FuncionDataInvalidException("Error en los datos enviados");
//        }
//
//        funcionesDB.updateFuncion(idFuncion, funcion);
//        return funcion;
//    }

    // Eliminar una función
//    public void deleteFuncion(int idFuncion) throws EntityNotFoundException {
//        FuncionesDB funcionesDB = new FuncionesDB();
//        Optional<Funcion> funcionOpt = funcionesDB.getById(idFuncion);
//        if (funcionOpt.isEmpty()) {
//            throw new EntityNotFoundException(
//                String.format("La función con id %d no existe", idFuncion)
//            );
//        }
//
//        funcionesDB.deleteFuncion(idFuncion);
//    }
}
