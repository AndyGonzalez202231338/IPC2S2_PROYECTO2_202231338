/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos.operacionCartera;

import java.math.BigDecimal;

/**
 *
 * @author andy
 */
public class DepositoRequest {
    private BigDecimal monto;

    public DepositoRequest() {}

    public DepositoRequest(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    
}
