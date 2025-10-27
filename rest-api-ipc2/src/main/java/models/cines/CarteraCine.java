/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.cines;

import java.math.BigDecimal;

/**
 *
 * @author andy
 */
public class CarteraCine {
    private int idCarteraCine;
    private int idCine;
    private BigDecimal saldo;

    public CarteraCine() {}

    public CarteraCine(int idCarteraCine, int idCine, BigDecimal saldo) {
        this.idCarteraCine = idCarteraCine;
        this.idCine = idCine;
        this.saldo = saldo;
    }

    public int getIdCarteraCine() {
        return idCarteraCine;
    }

    public void setIdCarteraCine(int idCarteraCine) {
        this.idCarteraCine = idCarteraCine;
    }

    public int getIdCine() {
        return idCine;
    }

    public void setIdCine(int idCine) {
        this.idCine = idCine;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    
}
