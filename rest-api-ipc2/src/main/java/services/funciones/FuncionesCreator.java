/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.funciones;

import db.FuncionDB;
import dtos.funciones.NewFuncionRequest;
import exceptions.EntityAlreadyExistsException;
import exceptions.UserDataInvalidException;
import models.funcion.Funcion;

/**
 *
 * @author andy
 */
public class FuncionesCreator {

    public Funcion createFuncion(NewFuncionRequest newFuncionRequest) throws UserDataInvalidException,
            EntityAlreadyExistsException {
        FuncionDB funcionDB = new FuncionDB();
        Funcion funcion = extractFuncion(newFuncionRequest);
        
        // Validar si ya existe una función en la misma sala y horario
        if (funcionDB.existeFuncionEnSalaYHorario(
                newFuncionRequest.getIdSala(), 
                newFuncionRequest.getFechaHoraFuncion())) {
            throw new EntityAlreadyExistsException("Ya existe una función programada en esta sala para el horario seleccionado");
        }
        if (funcionDB.existsFuncion(newFuncionRequest.getIdFuncion())) {
            throw new EntityAlreadyExistsException(
                    String.format("la funcion con id %s ya existe", funcion.getIdFuncion()));
        }

        funcionDB.createFuncion(funcion);

        return funcion;
    }

    private Funcion extractFuncion(NewFuncionRequest newFuncionRequest) throws UserDataInvalidException {
        try {
            
            Funcion funcion = new Funcion(
                    newFuncionRequest.getIdFuncion(),
                    newFuncionRequest.getIdSala(),
                    newFuncionRequest.getIdPelicula(),
                    newFuncionRequest.getFechaHoraFuncion(),
                    newFuncionRequest.getPrecioBoletoAdulto(),
                    newFuncionRequest.getPrecioBoletoNino(),
                    newFuncionRequest.getAsientosDisponibles(),
                    newFuncionRequest.getEstado()
            );

            if (!funcion.isValid()) {
                throw new UserDataInvalidException("Error en los datos enviados");
            }

            return funcion;
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new UserDataInvalidException("Error en los datos enviados");
        }
    }

}
