/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.cines;

import db.CarteraCineDB;
import exceptions.EntityNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import java.math.BigDecimal;
import java.util.Optional;
import models.cines.CarteraCine;

/**
 *
 * @author andy
 */
public class CarteraCineService {
    
    private CarteraCineDB carteraCineDB;
    
    public CarteraCineService() {
        this.carteraCineDB = new CarteraCineDB();
    }
    
    /** Crea una cartera para un cine (al crearse el cine) */
    public CarteraCine crearCarteraParaCine(int idCine) {
        // Verificar si ya existe
        if (carteraCineDB.existeCarteraPorCine(idCine)) {
            return carteraCineDB.obtenerCarteraPorCine(idCine).get();
        }
        
        // Crear nueva cartera
        return carteraCineDB.crearCarteraCine(idCine);
    }
    
    /** Obtener saldo de un cine */
    public BigDecimal consultarSaldoCine(int idCine) throws EntityNotFoundException {
        Optional<CarteraCine> carteraOpt = carteraCineDB.obtenerCarteraPorCine(idCine);
        if (carteraOpt.isEmpty()) {
            throw new EntityNotFoundException("El cine no tiene cartera digital");
        }
        return carteraOpt.get().getSaldo();
    }
    
    /** Depositar dinero en la cartera del cine */
    public CarteraCine depositarCine(int idCine, BigDecimal monto) 
            throws EntityNotFoundException, InvalidAmountException {
        
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("El monto debe ser mayor a cero");
        }
        
        Optional<CarteraCine> carteraOpt = carteraCineDB.obtenerCarteraPorCine(idCine);
        if (carteraOpt.isEmpty()) {
            throw new EntityNotFoundException("El cine no tiene cartera digital");
        }
        
        CarteraCine cartera = carteraOpt.get();
        BigDecimal nuevoSaldo = cartera.getSaldo().add(monto);
        
        carteraCineDB.actualizarSaldoCine(idCine, nuevoSaldo);
        cartera.setSaldo(nuevoSaldo);
        
        return cartera;
    }
    
    /** Retirar dinero de la cartera del cine */
    public CarteraCine retirarCine(int idCine, BigDecimal monto) 
            throws EntityNotFoundException, InvalidAmountException, InsufficientFundsException {
        
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("El monto debe ser mayor a cero");
        }
        
        Optional<CarteraCine> carteraOpt = carteraCineDB.obtenerCarteraPorCine(idCine);
        if (carteraOpt.isEmpty()) {
            throw new EntityNotFoundException("El cine no tiene cartera digital");
        }
        
        CarteraCine cartera = carteraOpt.get();
        
        if (cartera.getSaldo().compareTo(monto) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente para realizar el retiro");
        }
        
        BigDecimal nuevoSaldo = cartera.getSaldo().subtract(monto);
        carteraCineDB.actualizarSaldoCine(idCine, nuevoSaldo);
        cartera.setSaldo(nuevoSaldo);
        
        return cartera;
    }
    
    /** Recibir pago (aumentar saldo cuando un usuario compra) */
    public CarteraCine recibirPago(int idCine, BigDecimal montoPago) 
            throws EntityNotFoundException, InvalidAmountException {
        return depositarCine(idCine, montoPago); // Reutiliza la lógica de depósito
    }
    
    /** Pagar gastos (disminuir saldo) */
    public CarteraCine pagarGasto(int idCine, BigDecimal montoGasto) 
            throws EntityNotFoundException, InvalidAmountException, InsufficientFundsException {
        return retirarCine(idCine, montoGasto); // Reutiliza la lógica de retiro
    }
    
    /** Obtener cartera completa del cine */
    public CarteraCine obtenerCarteraCine(int idCine) throws EntityNotFoundException {
        Optional<CarteraCine> carteraOpt = carteraCineDB.obtenerCarteraPorCine(idCine);
        if (carteraOpt.isEmpty()) {
            throw new EntityNotFoundException("El cine no tiene cartera digital");
        }
        return carteraOpt.get();
    }
}