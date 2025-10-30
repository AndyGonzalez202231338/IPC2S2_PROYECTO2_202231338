package services.anuncios;

import db.AnunciosDB;
import dtos.anuncios.UpdateAnuncioRequest;
import models.anuncios.Anuncio;
import exceptions.EntityNotFoundException;
import exceptions.AnuncioDataInvalidException;
import java.util.List;
import java.util.Optional;
import java.io.InputStream;

/**
 *
 * @author andy
 */
public class AnunciosCrudService {
    
    public List<Anuncio> getAllAnuncios() {
        AnunciosDB anunciosDB = new AnunciosDB();
        return anunciosDB.getAllAnuncios();
    }
    
    public Anuncio getAnuncioById(int idAnuncio) throws EntityNotFoundException {
        AnunciosDB anunciosDB = new AnunciosDB();
        Optional<Anuncio> anuncioOpt = anunciosDB.getAnuncioById(idAnuncio);
        if (anuncioOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("El anuncio con id %s no existe", idAnuncio)
            );
        }
        return anuncioOpt.get();
    }
    
    public List<Anuncio> getAnunciosByUsuario(int idUsuario) {
        AnunciosDB anunciosDB = new AnunciosDB();
        return anunciosDB.getAnunciosByUsuario(idUsuario);
    }
    
    public Anuncio getAnuncioCompletoById(int idAnuncio) throws EntityNotFoundException {
        AnunciosDB anunciosDB = new AnunciosDB();
        Optional<Anuncio> anuncioOpt = anunciosDB.getAnuncioCompletoById(idAnuncio);
        if (anuncioOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("El anuncio con id %s no existe", idAnuncio)
            );
        }
        return anuncioOpt.get();
    }
    
    public List<Anuncio> getAnunciosCompletosByUsuario(int idUsuario) {
        AnunciosDB anunciosDB = new AnunciosDB();
        return anunciosDB.getAnunciosCompletosByUsuario(idUsuario);
    }
    
    public Anuncio updateAnuncio(int idAnuncio, UpdateAnuncioRequest updateAnuncioRequest) 
            throws AnuncioDataInvalidException, EntityNotFoundException {
        
        AnunciosDB anunciosDB = new AnunciosDB();
        
        // Obtener el anuncio existente
        Anuncio anuncio = getAnuncioById(idAnuncio);
        
        // Actualizar campos
        if (updateAnuncioRequest.getTitulo() != null) {
            anuncio.setTitulo(updateAnuncioRequest.getTitulo());
        }
        
        if (updateAnuncioRequest.getContenidoTexto() != null) {
            anuncio.setContenidoTexto(updateAnuncioRequest.getContenidoTexto());
        }
        
        if (updateAnuncioRequest.getImagenUrl() != null) {
            anuncio.setImagenUrl(updateAnuncioRequest.getImagenUrl());
        }
        
        if (updateAnuncioRequest.getVideoUrl() != null) {
            anuncio.setVideoUrl(updateAnuncioRequest.getVideoUrl());
        }
        
        if (updateAnuncioRequest.getEstado() != null) {
            anuncio.setEstado(updateAnuncioRequest.getEstado());
        }
        
        // Validar que el anuncio siga siendo válido
        if (!anuncio.isValid()) {
            throw new AnuncioDataInvalidException("Error en los datos del anuncio actualizados");
        }
        
        // Actualizar en la base de datos
        anunciosDB.updateAnuncio(anuncio);
        
        return anuncio;
    }

    // ========== NUEVOS MÉTODOS PARA MANEJO DE IMÁGENES ==========
    
    /**
     * Obtiene la imagen de un anuncio como array de bytes
     */
    public byte[] getImagenAnuncio(int idAnuncio) throws EntityNotFoundException {
        AnunciosDB anunciosDB = new AnunciosDB();
        Optional<Anuncio> anuncioOpt = anunciosDB.getAnuncioById(idAnuncio);
        if (anuncioOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("El anuncio con id %s no existe", idAnuncio)
            );
        }
        
        // Este método deberá implementarse en AnunciosDB
        return anunciosDB.getImagenAnuncio(idAnuncio);
    }
    
    /**
     * Actualiza un anuncio con nueva imagen
     */
    public Anuncio updateAnuncioConImagen(int idAnuncio, UpdateAnuncioRequest updateAnuncioRequest, InputStream imagenInputStream) 
            throws AnuncioDataInvalidException, EntityNotFoundException {
        
        AnunciosDB anunciosDB = new AnunciosDB();
        
        // Obtener el anuncio existente
        Anuncio anuncio = getAnuncioById(idAnuncio);
        
        // Actualizar campos (sin imagen_url ya que se maneja por InputStream)
        if (updateAnuncioRequest.getTitulo() != null) {
            anuncio.setTitulo(updateAnuncioRequest.getTitulo());
        }
        
        if (updateAnuncioRequest.getContenidoTexto() != null) {
            anuncio.setContenidoTexto(updateAnuncioRequest.getContenidoTexto());
        }
        
        if (updateAnuncioRequest.getVideoUrl() != null) {
            anuncio.setVideoUrl(updateAnuncioRequest.getVideoUrl());
        }
        
        if (updateAnuncioRequest.getEstado() != null) {
            anuncio.setEstado(updateAnuncioRequest.getEstado());
        }
        
        // Validar que el anuncio siga siendo válido
        if (!anuncio.isValid()) {
            throw new AnuncioDataInvalidException("Error en los datos del anuncio actualizados");
        }
        
        // Actualizar en la base de datos CON IMAGEN
        anunciosDB.updateAnuncioConImagen(anuncio, imagenInputStream);
        
        return anuncio;
    }
    
    /**
     * Actualiza solo la imagen de un anuncio (sin modificar otros campos)
     */
    public void actualizarImagenAnuncio(int idAnuncio, InputStream imagenInputStream) 
            throws EntityNotFoundException {
        
        AnunciosDB anunciosDB = new AnunciosDB();
        
        // Verificar que el anuncio existe
        getAnuncioById(idAnuncio);
        
        // Actualizar solo la imagen
        anunciosDB.actualizarImagenAnuncio(idAnuncio, imagenInputStream);
    }
    
    public void deleteAnuncio(int idAnuncio) throws EntityNotFoundException {
        AnunciosDB anunciosDB = new AnunciosDB();
        Optional<Anuncio> anuncioOpt = anunciosDB.getAnuncioById(idAnuncio);
        if (anuncioOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("El anuncio con id %s no existe", idAnuncio)
            );
        }
        anunciosDB.deleteAnuncio(idAnuncio);
    }
}