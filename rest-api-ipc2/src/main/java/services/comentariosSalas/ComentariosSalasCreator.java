/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.comentariosSalas;

import db.ComentariosSalasDB;
import dtos.comentariosSalas.NewComentarioSalaRequest;
import exceptions.EntityAlreadyExistsException;
import exceptions.UserDataInvalidException;
import models.comentario.ComentarioSala;

/**
 *
 * @author andy
 */
public class ComentariosSalasCreator {

    public ComentarioSala createUser(NewComentarioSalaRequest NewComentarioSalaRequest) throws UserDataInvalidException,
            EntityAlreadyExistsException {
        ComentariosSalasDB comentariosSalasDB = new ComentariosSalasDB();
        ComentarioSala comentarioSala = extractComentarioSalar(NewComentarioSalaRequest);

        // CORREGIDO: Pasar ambos parámetros (idUsuario e idSala)
        if (comentariosSalasDB.existsComentarioSala(
                NewComentarioSalaRequest.getIdUsuario(),
                NewComentarioSalaRequest.getIdSala())) {
            throw new EntityAlreadyExistsException(
                    String.format("El usuario con id %s ya tiene un comentario para esta sala", comentarioSala.getIdUsuario()));
        }

        // Crear el comentario
        comentariosSalasDB.createComentarioSala(comentarioSala);

        return comentarioSala;
    }

    private ComentarioSala extractComentarioSalar(NewComentarioSalaRequest NewComentarioSalaRequest) throws UserDataInvalidException {
        try {

            ComentarioSala comentarioSala = new ComentarioSala(
                    NewComentarioSalaRequest.getIdComentarioSala(),
                    NewComentarioSalaRequest.getIdSala(),
                    NewComentarioSalaRequest.getIdUsuario(),
                    NewComentarioSalaRequest.getComentario(),
                    NewComentarioSalaRequest.getCalificacion(),
                    NewComentarioSalaRequest.getFechaComentario(),
                    NewComentarioSalaRequest.getEstado()
            );

            if (!comentarioSala.isValid()) {
                throw new UserDataInvalidException("Error en los datos enviados");
            }

            return comentarioSala;
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new UserDataInvalidException("Error en los datos enviados");
        }
    }
}
