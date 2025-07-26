package com.asmdev.api.pos.dto;

import com.asmdev.api.pos.persistence.entity.SaleEntity;
import com.asmdev.api.pos.utils.status.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDto implements Serializable {

    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no debe superar los 50 caracteres")
    private String name;


    private String lastname;

    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos numéricos")
    private String phone;

    @NotNull(message = "El estatus es obligatorio")
    private Status status;

    private List<SaleEntity> sales;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<SaleEntity> getSales() {
        return sales;
    }

    public void setSales(List<SaleEntity> sales) {
        this.sales = sales;
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
}
