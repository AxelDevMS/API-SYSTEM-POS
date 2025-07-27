package com.asmdev.api.pos.dto;

import com.asmdev.api.pos.dto.CashRegister.CashRegisterDto;
import com.asmdev.api.pos.utils.status.CashMovementsStatus;
import com.asmdev.api.pos.utils.status.TypeCashMovement;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CashMovementsDto implements Serializable {

    private String id;

    private TypeCashMovement type;

    private String referenceType;

    private String referenceId;

    private String concept;

    private BigDecimal amount;

    private CashMovementsStatus status;

    private CashRegisterDto cashRegister;

    private UserDto user;

    private String cancelledUser;

    private Date cancelledAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CashRegisterDto getCashRegister() {
        return cashRegister;
    }

    public void setCashRegister(CashRegisterDto cashRegister) {
        this.cashRegister = cashRegister;
    }

    public TypeCashMovement getType() {
        return type;
    }

    public void setType(TypeCashMovement type) {
        this.type = type;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public CashMovementsStatus getStatus() {
        return status;
    }

    public void setStatus(CashMovementsStatus status) {
        this.status = status;
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

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getCancelledUser() {
        return cancelledUser;
    }

    public void setCancelledUser(String cancelledUser) {
        this.cancelledUser = cancelledUser;
    }

    public Date getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Date cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}
