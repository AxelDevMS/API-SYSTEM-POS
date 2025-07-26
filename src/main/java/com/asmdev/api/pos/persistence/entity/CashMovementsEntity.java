package com.asmdev.api.pos.persistence.entity;

import com.asmdev.api.pos.utils.status.CashMovementsStatus;
import com.asmdev.api.pos.utils.status.TypeCashMovement;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "cash_movement")
public class CashMovementsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "cash_register_id")
    private CashRegisterEntity cashRegister;

    private TypeCashMovement type;

    private String referenceType;

    private String referenceId;

    private String concept;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private CashMovementsStatus status;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CashRegisterEntity getCashRegister() {
        return cashRegister;
    }

    public void setCashRegister(CashRegisterEntity cashRegister) {
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
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
}
