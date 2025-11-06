/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.boletos;

import db.BoletosDB;
import exceptions.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import models.Boleto;

/**
 *
 * @author andy
 */
public class BoletoCrudService {
    // Método existente
    public List<Boleto> getBoletosByUsuario(int idUsuario) throws EntityNotFoundException {
        BoletosDB boletosDB = new BoletosDB();
        List<Boleto> boletos = boletosDB.getBoletosByUsuario(idUsuario);
        
        if (boletos.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron boletos para el usuario: " + idUsuario);
        }
        
        return boletos;
    }
    
    // Nuevo método para obtener boletos únicos por función
    public List<Boleto> getBoletosUnicosByUsuario(int idUsuario) throws EntityNotFoundException {
        List<Boleto> todosBoletos = getBoletosByUsuario(idUsuario);
        
        // Filtrar para obtener solo un boleto por función
        List<Boleto> boletosUnicos = new ArrayList<>();
        Set<Integer> funcionesProcesadas = new HashSet<>();
        
        for (Boleto boleto : todosBoletos) {
            if (!funcionesProcesadas.contains(boleto.getIdFuncion())) {
                boletosUnicos.add(boleto);
                funcionesProcesadas.add(boleto.getIdFuncion());
            }
        }
        
        if (boletosUnicos.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron boletos para el usuario: " + idUsuario);
        }
        
        return boletosUnicos;
    }
}
