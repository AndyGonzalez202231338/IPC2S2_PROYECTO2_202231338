/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.boletos;

import db.BoletosDB;
import dtos.boletos.NewBoletoRequest;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import exceptions.SaldoInsuficienteException;
import exceptions.UserDataInvalidException;
import java.math.BigDecimal;
import models.Boleto;
import services.users.CarteraDigitalService;

/**
 *
 * @author andy
 */
public class BoletosCreator {
    public Boleto createBoleto(NewBoletoRequest newBoletoRequest) throws UserDataInvalidException,
        EntityAlreadyExistsException,
        SaldoInsuficienteException,
        EntityNotFoundException,
        InvalidAmountException,
        InsufficientFundsException {
    BoletosDB boletosDB = new BoletosDB();
    CarteraDigitalService carteraService = new CarteraDigitalService();
    Boleto boleto = extractBoleto(newBoletoRequest);
    
    
    // VALIDAR SALDO SUFICIENTE
        validarSaldoSuficiente(boleto.getIdUsuario(), BigDecimal.valueOf(boleto.getPrecioPagado()));
        
        // DESCONTAR DE LA CARTERA DIGITAL
        carteraService.retirar(boleto.getIdUsuario(), BigDecimal.valueOf(boleto.getPrecioPagado()));
    
    boletosDB.createBoleto(boleto);
    
    
    return boleto;
}
    
    private Boleto extractBoleto(NewBoletoRequest newBoletoRequest) throws UserDataInvalidException {
        try {
            
            Boleto boleto = new Boleto(
                    newBoletoRequest.getIdBoleto(),
                    newBoletoRequest.getIdFuncion(),
                    newBoletoRequest.getIdUsuario(),
                    newBoletoRequest.getCodigoBoleto(),
                    newBoletoRequest.getFechaCompra(),
                    newBoletoRequest.getPrecioPagado()
            );
            
            if (!boleto.isValid()) {
                throw new UserDataInvalidException("Error en los datos enviados");
            }
            
            return boleto;
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new UserDataInvalidException("Error en los datos enviados");
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
}
