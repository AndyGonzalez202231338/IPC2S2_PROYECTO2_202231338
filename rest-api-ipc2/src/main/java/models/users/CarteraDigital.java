/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.users;

import java.math.BigDecimal;

/**
 *
 * @author andy
 */
public class CarteraDigital {
    private int idCartera;
    private int idUsuario;
    private BigDecimal saldo;

    public CarteraDigital() {}

    public CarteraDigital(int idCartera, int idUsuario, BigDecimal saldo) {
        this.idCartera = idCartera;
        this.idUsuario = idUsuario;
        this.saldo = saldo;
    }

    public int getIdCartera() {
        return idCartera;
    }

    public void setIdCartera(int idCartera) {
        this.idCartera = idCartera;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    
}