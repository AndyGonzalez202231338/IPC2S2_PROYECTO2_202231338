/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos.operacionCarteraCine;

import java.math.BigDecimal;

/**
 *
 * @author andy
 */
public class DepositoCineRequest {
    private BigDecimal monto;

    public DepositoCineRequest() {}

    public DepositoCineRequest(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    
}
