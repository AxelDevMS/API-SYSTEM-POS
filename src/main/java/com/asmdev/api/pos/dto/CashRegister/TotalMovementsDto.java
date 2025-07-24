package com.asmdev.api.pos.dto.CashRegister;

import java.math.BigDecimal;

public class TotalMovementsDto {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal totalSales;

    public TotalMovementsDto(){

    }

    public TotalMovementsDto(BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal totalSales) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.totalSales = totalSales;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }
}
