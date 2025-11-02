package services.bloqueo;

import db.BloqueoPublicidadDB;
import dtos.bloqueo.NewBloqueoRequest;
import exceptions.EntityAlreadyExistsException;
import models.bloqueo.BloqueoPublicidad;
import java.time.LocalDate;
import services.publicidad.PublicidadService;

public class BloqueoPublicidadCreator {
    
    public BloqueoPublicidad createBloqueo(NewBloqueoRequest newBloqueoRequest) 
        throws EntityAlreadyExistsException {
    
    try {
        BloqueoPublicidadDB bloqueoPublicidadDB = new BloqueoPublicidadDB();
        
        // Verificar si este cine ya tiene un bloqueo activo para este anuncio
        boolean existeBloqueo = bloqueoPublicidadDB.existsBloqueoByCineAndAnuncio(
                newBloqueoRequest.getIdCine(),
                newBloqueoRequest.getIdPublicidad());
        
        
        if (existeBloqueo) {
            throw new EntityAlreadyExistsException(
                String.format("El cine %s ya tiene un bloqueo activo para esta publicidad", 
                    newBloqueoRequest.getIdCine()));
        }
        
        // Verificar si el anuncio está disponible
        boolean disponible = bloqueoPublicidadDB.isAnuncioDisponible(newBloqueoRequest.getIdPublicidad());
        System.out.println("🔍 ¿Anuncio disponible?: " + disponible);
        
        if (!disponible) {
            throw new EntityAlreadyExistsException(
                String.format("El anuncio %s no está disponible para bloqueo", 
                    newBloqueoRequest.getIdPublicidad()));
        }
        
        BloqueoPublicidad bloqueo = extractBloqueo(newBloqueoRequest);
        System.out.println("🔍 Bloqueo extraído: " + bloqueo);
        
        // Crear el bloqueo
        BloqueoPublicidad bloqueoCreado = bloqueoPublicidadDB.create(bloqueo);
        
        // ACTUALIZAR ESTADO DE LA PUBLICIDAD
        PublicidadService publicidadService = new PublicidadService();
        boolean estadoActualizado = publicidadService.actualizarEstadoPublicidad(
            newBloqueoRequest.getIdPublicidad(), 
            "BLOQUEADO");
        
        
        return bloqueoCreado;
        
    } catch (Exception e) {
        e.printStackTrace();
        throw e; // Relanzar la excepción
    }
}
    
    private BloqueoPublicidad extractBloqueo(NewBloqueoRequest newBloqueoRequest) {
    try {
        BloqueoPublicidad bloqueo = new BloqueoPublicidad(
            newBloqueoRequest.getIdCine(),
            newBloqueoRequest.getIdPublicidad(),
            null,  // fecha_inicio = NULL
            null,  // fecha_fin = NULL
            newBloqueoRequest.getCostoTotal()
        );
        
        
        return bloqueo;
    } catch (IllegalArgumentException | NullPointerException e) {
        throw new IllegalArgumentException("Error en los datos enviados para el bloqueo: " + e.getMessage());
    }
}
}