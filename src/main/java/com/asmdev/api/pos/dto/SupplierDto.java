package com.asmdev.api.pos.dto;

import com.asmdev.api.pos.persistence.entity.PurchaseEntity;
import com.asmdev.api.pos.utils.status.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierDto implements Serializable {

    private String id;

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String name;

    @NotBlank(message = "El nombre del contacto es obligatorio")
    @Size(max = 100, message = "El nombre del contacto no debe superar los 100 caracteres")
    private String contactPerson;

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos")
    private String phone;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no tiene un formato válido")
    private String email;

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 100, message = "La calle no debe superar los 100 caracteres")
    private String street;

    @NotBlank(message = "El número exterior es obligatorio")
    @Size(max = 10, message = "El número exterior no debe superar los 10 caracteres")
    private String exteriorNumber;

    @Size(max = 10, message = "El número interior no debe superar los 10 caracteres")
    private String interiorNumber; // Puede ser null

    @NotBlank(message = "La colonia es obligatoria")
    @Size(max = 100, message = "La colonia no debe superar los 100 caracteres")
    private String neighborhood;

    @NotBlank(message = "El municipio es obligatorio")
    @Size(max = 100, message = "El municipio no debe superar los 100 caracteres")
    private String municipality;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 100, message = "El estado no debe superar los 100 caracteres")
    private String state;

    @NotNull(message = "El estatus es obligatorio")
    private Status status;

    private List<PurchaseEntity> purchases;

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

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getExteriorNumber() {
        return exteriorNumber;
    }

    public void setExteriorNumber(String exteriorNumber) {
        this.exteriorNumber = exteriorNumber;
    }

    public String getInteriorNumber() {
        return interiorNumber;
    }

    public void setInteriorNumber(String interiorNumber) {
        this.interiorNumber = interiorNumber;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<PurchaseEntity> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseEntity> purchases) {
        this.purchases = purchases;
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
