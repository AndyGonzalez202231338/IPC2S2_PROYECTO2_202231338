/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.users;

import db.CarteraDigitalDB;
import exceptions.EntityNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import java.math.BigDecimal;
import java.util.Optional;
import models.users.CarteraDigital;

/**
 *
 * @author andy
 */
public class CarteraDigitalService {
    
    private CarteraDigitalDB carteraDB;
    
    public CarteraDigitalService() {
        this.carteraDB = new CarteraDigitalDB();
    }
    
    /** Crea una cartera para un usuario (al registrarse) */
    public CarteraDigital crearCarteraParaUsuario(int idUsuario) {
        // Verificar si ya existe
        if (carteraDB.existeCarteraPorUsuario(idUsuario)) {
            return carteraDB.obtenerCarteraPorUsuario(idUsuario).get();
        }
        
        // Crear nueva cartera
        return carteraDB.crearCartera(idUsuario);
    }
    
    /** Obtener saldo de un usuario */
    public BigDecimal consultarSaldo(int idUsuario) throws EntityNotFoundException {
        Optional<CarteraDigital> carteraOpt = carteraDB.obtenerCarteraPorUsuario(idUsuario);
        if (carteraOpt.isEmpty()) {
            throw new EntityNotFoundException("El usuario no tiene cartera digital");
        }
        return carteraOpt.get().getSaldo();
    }
    
    /** Depositar dinero en la cartera */
    public CarteraDigital depositar(int idUsuario, BigDecimal monto) 
            throws EntityNotFoundException, InvalidAmountException {
        
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("El monto debe ser mayor a cero");
        }
        
        Optional<CarteraDigital> carteraOpt = carteraDB.obtenerCarteraPorUsuario(idUsuario);
        if (carteraOpt.isEmpty()) {
            throw new EntityNotFoundException("El usuario no tiene cartera digital");
        }
        
        CarteraDigital cartera = carteraOpt.get();
        BigDecimal nuevoSaldo = cartera.getSaldo().add(monto);
        
        carteraDB.actualizarSaldo(idUsuario, nuevoSaldo);
        cartera.setSaldo(nuevoSaldo);
        
        return cartera;
    }
    
    /** Retirar dinero de la cartera */
    public CarteraDigital retirar(int idUsuario, BigDecimal monto) 
            throws EntityNotFoundException, InvalidAmountException, InsufficientFundsException {
        
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("El monto debe ser mayor a cero");
        }
        
        Optional<CarteraDigital> carteraOpt = carteraDB.obtenerCarteraPorUsuario(idUsuario);
        if (carteraOpt.isEmpty()) {
            throw new EntityNotFoundException("El usuario no tiene cartera digital");
        }
        
        CarteraDigital cartera = carteraOpt.get();
        
        if (cartera.getSaldo().compareTo(monto) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente para realizar el retiro");
        }
        
        BigDecimal nuevoSaldo = cartera.getSaldo().subtract(monto);
        carteraDB.actualizarSaldo(idUsuario, nuevoSaldo);
        cartera.setSaldo(nuevoSaldo);
        
        return cartera;
    }
    
    /** Realizar compra (disminuir saldo) */
    public CarteraDigital realizarCompra(int idUsuario, BigDecimal montoCompra) 
            throws EntityNotFoundException, InvalidAmountException, InsufficientFundsException {
        return retirar(idUsuario, montoCompra); // Reutiliza la lógica de retiro
    }
    
    /** Obtener cartera completa */
    public CarteraDigital obtenerCartera(int idUsuario) throws EntityNotFoundException {
        Optional<CarteraDigital> carteraOpt = carteraDB.obtenerCarteraPorUsuario(idUsuario);
        if (carteraOpt.isEmpty()) {
            throw new EntityNotFoundException("El usuario no tiene cartera digital");
        }
        return carteraOpt.get();
    }
}