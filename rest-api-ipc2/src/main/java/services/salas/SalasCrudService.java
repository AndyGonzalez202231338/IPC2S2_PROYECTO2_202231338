package services.salas;

import db.SalasDB;
import dtos.salas.UpdateSalaRequest;
import exceptions.EntityNotFoundException;
import models.salas.Sala;

import java.util.List;
import java.util.Optional;

public class SalasCrudService {

    private final SalasDB salasDB;

    public SalasCrudService() {
        this.salasDB = new SalasDB();
    }

    public List<Sala> getAllSalas() {
        return salasDB.findAll();
    }

    public List<Sala> getSalasByCine(int idCine) {
        return salasDB.findByCineId(idCine);
    }

    public Sala getSalaById(int idSala) throws EntityNotFoundException {
    SalasDB salaDB = new SalasDB();
    Optional<Sala> salaOpt = salaDB.findById(idSala);
    if (salaOpt.isEmpty()) {
        throw new EntityNotFoundException(
            String.format("No existe la sala con id %d", idSala)
        );
    }
    return salaOpt.get();
}

    public Sala updateSala(int idSala, UpdateSalaRequest salaRequest) throws EntityNotFoundException {
        return salasDB.findById(idSala)
                .map(salaExistente -> {
                    // Actualizar solo los campos permitidos
                    if (salaRequest.getPermiteComentario() != null) {
                        salaExistente.setPermiteComentario(salaRequest.getPermiteComentario());
                    }
                    if (salaRequest.getEstado() != null) {
                        salaExistente.setEstado(salaRequest.getEstado());
                    }

                    // Validar que los datos sean correctos
                    if (!salaExistente.isValid()) {
                        throw new IllegalArgumentException("Datos de sala no válidos después de la actualización");
                    }

                    return salasDB.update(salaExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Sala no encontrada con id: " + idSala));
    }
}
