package services.bloqueo;

import db.BloqueoPublicidadDB;
import exceptions.EntityNotFoundException;
import models.bloqueo.BloqueoPublicidad;

import java.time.LocalDate;
import java.util.List;

public class BloqueoPublicidadCrudService {
    
    private final BloqueoPublicidadDB bloqueoPublicidadDB;
    
    public BloqueoPublicidadCrudService() {
        this.bloqueoPublicidadDB = new BloqueoPublicidadDB();
    }
    
    public List<BloqueoPublicidad> getAllBloqueos() {
        return bloqueoPublicidadDB.findAll();
    }
    
    public BloqueoPublicidad getBloqueoById(int idBloqueo) throws EntityNotFoundException {
        return bloqueoPublicidadDB.findById(idBloqueo)
                .orElseThrow(() -> new EntityNotFoundException("Bloqueo no encontrado con id: " + idBloqueo));
    }
    
    public BloqueoPublicidad getBloqueoByAnuncioAndCine(int idAnuncio, int idCine) throws EntityNotFoundException {
        return bloqueoPublicidadDB.findByAnuncioAndCine(idAnuncio, idCine)
                .orElseThrow(() -> new EntityNotFoundException("Bloqueo no encontrado para anuncio: " + idAnuncio + " y cine: " + idCine));
    }
    
    public List<BloqueoPublicidad> getBloqueosByCine(int idCine) {
        return bloqueoPublicidadDB.findByCineId(idCine);
    }
    
    public List<BloqueoPublicidad> getBloqueosByPublicidad(int idPublicidad) {
        return bloqueoPublicidadDB.findByPublicidadId(idPublicidad);
    }
    
    public List<BloqueoPublicidad> getBloqueosActivosByAnuncio(int idAnuncio) {
        LocalDate hoy = LocalDate.now();
        return bloqueoPublicidadDB.findBloqueosActivosByAnuncio(idAnuncio, hoy);
    }
    
    public List<BloqueoPublicidad> getBloqueosActivosByCine(int idCine) {
        LocalDate hoy = LocalDate.now();
        return bloqueoPublicidadDB.findBloqueosActivosByCine(idCine, hoy);
    }
    
    public boolean verificarDisponibilidadAnuncio(int idAnuncio) throws EntityNotFoundException {
        List<BloqueoPublicidad> bloqueosActivos = getBloqueosActivosByAnuncio(idAnuncio);
        return bloqueosActivos.isEmpty();
    }
}