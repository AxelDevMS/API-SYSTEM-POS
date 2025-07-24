package com.asmdev.api.pos.dto;

import com.asmdev.api.pos.utils.status.CashRegisterStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CashRegisterDto implements Serializable {

    private String id;

    @NotNull(message = "El monto de apertura es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El monto de apertura debe ser mayor o igual a 0")
    private BigDecimal openingAmount;

    private BigDecimal currentAmount;

    private BigDecimal closingAmount;

    private BigDecimal expectedAmount;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date openedAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date closedAt;

    private CashRegisterStatus status;

    @Size(max = 500, message = "Las notas deben tener como máximo 500 caracteres")
    private String notes;

    private Integer totalSaleMovements;

    private  Integer totalExpenseMovements;

    private List<CashMovementsDto> movements;

    private UserDto user;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date updatedAt;

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public BigDecimal getOpeningAmount() {
        return openingAmount;
    }

    public void setOpeningAmount(BigDecimal openingAmount) {
        this.openingAmount = openingAmount;
    }

    public BigDecimal getClosingAmount() {
        return closingAmount;
    }

    public void setClosingAmount(BigDecimal closingAmount) {
        this.closingAmount = closingAmount;
    }

    public BigDecimal getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(BigDecimal expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    public Date getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(Date openedAt) {
        this.openedAt = openedAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public CashRegisterStatus getStatus() {
        return status;
    }

    public void setStatus(CashRegisterStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<CashMovementsDto> getMovements() {
        return movements;
    }

    public void setMovements(List<CashMovementsDto> movements) {
        this.movements = movements;
    }

    public Integer getTotalSaleMovements() {
        return totalSaleMovements;
    }

    public void setTotalSaleMovements(Integer totalSaleMovements) {
        this.totalSaleMovements = totalSaleMovements;
    }

    public Integer getTotalExpenseMovements() {
        return totalExpenseMovements;
    }

    public void setTotalExpenseMovements(Integer totalExpenseMovements) {
        this.totalExpenseMovements = totalExpenseMovements;
    }
}
