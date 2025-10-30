package services.anuncios;

import db.AnunciosDB;
import db.CarteraDigitalDB;
import dtos.anuncios.NewAnuncioRequest;
import models.anuncios.Anuncio;
import exceptions.EntityAlreadyExistsException;
import exceptions.AnuncioDataInvalidException;
import exceptions.EntityNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import exceptions.SaldoInsuficienteException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.InputStream;
import services.users.CarteraDigitalService;

/**
 *
 * @author andy
 */
public class AnunciosCreator {

    public Anuncio createAnuncio(NewAnuncioRequest newAnuncioRequest) 
            throws AnuncioDataInvalidException, EntityAlreadyExistsException, SaldoInsuficienteException, EntityNotFoundException, InvalidAmountException, InsufficientFundsException {
        
        AnunciosDB anunciosDB = new AnunciosDB();
        CarteraDigitalService carteraService = new CarteraDigitalService();
        
        // Extraer y validar el anuncio
        Anuncio anuncio = extractAnuncio(newAnuncioRequest);
        
        // Verificar si el anuncio ya existe
        if (anunciosDB.existsAnuncio(anuncio.getTitulo(), anuncio.getIdUsuario())) {
            throw new EntityAlreadyExistsException(
                    String.format("El usuario %s ya tiene un anuncio con el título '%s'", 
                            anuncio.getIdUsuario(), anuncio.getTitulo()));
        }
        
        // VALIDAR SALDO SUFICIENTE
        validarSaldoSuficiente(anuncio.getIdUsuario(), anuncio.getCostoTotal());
        
        // DESCONTAR DE LA CARTERA DIGITAL
        carteraService.retirar(anuncio.getIdUsuario(), anuncio.getCostoTotal());
        
        // Crear el anuncio
        anunciosDB.createAnuncio(anuncio);
        
        return anuncio;
    }

    // NUEVO MÉTODO: Crear anuncio con imagen
    public Anuncio createAnuncioConImagen(NewAnuncioRequest newAnuncioRequest, InputStream imagenInputStream) 
            throws AnuncioDataInvalidException, EntityAlreadyExistsException, SaldoInsuficienteException, EntityNotFoundException, InvalidAmountException, InsufficientFundsException {
        
        AnunciosDB anunciosDB = new AnunciosDB();
        CarteraDigitalService carteraService = new CarteraDigitalService();
        
        // Extraer y validar el anuncio
        Anuncio anuncio = extractAnuncio(newAnuncioRequest);
        
        // Verificar si el anuncio ya existe
        if (anunciosDB.existsAnuncio(anuncio.getTitulo(), anuncio.getIdUsuario())) {
            throw new EntityAlreadyExistsException(
                    String.format("El usuario %s ya tiene un anuncio con el título '%s'", 
                            anuncio.getIdUsuario(), anuncio.getTitulo()));
        }
        
        // VALIDAR SALDO SUFICIENTE
        validarSaldoSuficiente(anuncio.getIdUsuario(), anuncio.getCostoTotal());
        
        // DESCONTAR DE LA CARTERA DIGITAL
        carteraService.retirar(anuncio.getIdUsuario(), anuncio.getCostoTotal());
        
        // Crear el anuncio CON IMAGEN
        anunciosDB.createAnuncioConImagen(anuncio, imagenInputStream);
        
        return anuncio;
    }
    
    private Anuncio extractAnuncio(NewAnuncioRequest newAnuncioRequest) throws AnuncioDataInvalidException {
        try {
            // Validar y calcular fechas si es necesario
            LocalDateTime fechaInicio = newAnuncioRequest.getFechaInicio() != null 
                    ? newAnuncioRequest.getFechaInicio() 
                    : LocalDateTime.now();
            
            LocalDateTime fechaFin = newAnuncioRequest.getFechaFin() != null 
                    ? newAnuncioRequest.getFechaFin() 
                    : calcularFechaFin(fechaInicio, newAnuncioRequest.getIdPeriodo());
            
            // Validar que el costo total sea positivo
            if (newAnuncioRequest.getCostoTotal().compareTo(BigDecimal.ZERO) <= 0) {
                throw new AnuncioDataInvalidException("El costo total debe ser mayor a cero");
            }
            
            // Validar que la fecha fin sea posterior a la fecha inicio
            if (fechaFin.isBefore(fechaInicio)) {
                throw new AnuncioDataInvalidException("La fecha fin debe ser posterior a la fecha inicio");
            }
            
            Anuncio anuncio = new Anuncio(
                    newAnuncioRequest.getIdUsuario(),
                    newAnuncioRequest.getIdTipoAnuncio(),
                    newAnuncioRequest.getIdPeriodo(),
                    newAnuncioRequest.getTitulo(),
                    newAnuncioRequest.getContenidoTexto(),
                    null, // imagen_url será null cuando se use con imagen InputStream
                    newAnuncioRequest.getVideoUrl(),
                    fechaInicio,
                    fechaFin,
                    newAnuncioRequest.getCostoTotal(),
                    newAnuncioRequest.getEstado() != null ? newAnuncioRequest.getEstado() : "ACTIVO"
            );
            
            if (!anuncio.isValid()) {
                throw new AnuncioDataInvalidException("Error en los datos del anuncio enviados");
            }
            
            return anuncio;
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new AnuncioDataInvalidException("Error en los datos del anuncio enviados: " + e.getMessage());
        }
    }
    
    private void validarSaldoSuficiente(int idUsuario, BigDecimal costoTotal) throws SaldoInsuficienteException, EntityNotFoundException {
        CarteraDigitalService carteraService = new CarteraDigitalService();
        BigDecimal saldoActual = carteraService.consultarSaldo(idUsuario);
        
        if (saldoActual.compareTo(costoTotal) < 0) {
            throw new SaldoInsuficienteException(
                String.format("Saldo insuficiente. Saldo actual: $%.2f, Costo requerido: $%.2f", 
                             saldoActual, costoTotal)
            );
        }
    }
    
    private LocalDateTime calcularFechaFin(LocalDateTime fechaInicio, int idPeriodo) {
        // Por simplicidad, asumimos 7 días. En producción, consultarías la base de datos
        return fechaInicio.plusDays(7);
    }
}