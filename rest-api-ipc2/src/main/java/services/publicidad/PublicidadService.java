package services.publicidad;

import db.PublicidadDB;
import exceptions.EntityNotFoundException;
import exceptions.InvalidAmountException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import models.publicidad.Publicidad;

/**
 *
 * @author andy
 */
public class PublicidadService {
    
    private final PublicidadDB publicidadDB;
    
    public PublicidadService() {
        this.publicidadDB = new PublicidadDB();
    }
    
    public Publicidad crearPublicidad(int idAnuncio, int idUsuario, BigDecimal precioBloqueo) 
            throws EntityNotFoundException, InvalidAmountException {
        
        // Validar que el precio sea mayor a 0
        if (precioBloqueo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("El precio de bloqueo debe ser mayor a 0");
        }
        
        // Verificar que el anuncio existe (puedes agregar esta validación)
        // if (!anuncioExiste(idAnuncio)) {
        //     throw new EntityNotFoundException("Anuncio no encontrado");
        // }
        
        // Verificar que el usuario existe (puedes agregar esta validación)
        // if (!usuarioExiste(idUsuario)) {
        //     throw new EntityNotFoundException("Usuario no encontrado");
        // }
        
        // Verificar que el anuncio no tenga ya una publicidad
        if (publicidadDB.existePublicidadPorAnuncio(idAnuncio)) {
            throw new InvalidAmountException("El anuncio ya tiene una publicidad asignada");
        }
        
        // Crear la publicidad
        return publicidadDB.crearPublicidad(idAnuncio, idUsuario, precioBloqueo);
    }
    
    public List<Publicidad> getAllPublicidades() {
        return publicidadDB.getAllPublicidades();
    }
    
    public Optional<Publicidad> getPublicidadByAnuncioId(int idAnuncio) {
        return publicidadDB.getPublicidadByAnuncioId(idAnuncio);
    }
    
    public boolean actualizarEstadoPublicidad(int idPublicidad, String nuevoEstado) {
        // Validar estado
        if (!isEstadoValido(nuevoEstado)) {
            throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
        }
        
        return publicidadDB.actualizarEstado(idPublicidad, nuevoEstado);
    }
    
     private boolean isEstadoValido(String estado) {
        return estado != null && 
               (estado.equals("ACTIVO") || estado.equals("INACTIVO") || estado.equals("BLOQUEADO") || estado.equals("VENCIDO"));
    }
    
}