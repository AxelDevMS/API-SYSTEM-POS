package com.asmdev.api.pos.persistence.entity;

import com.asmdev.api.pos.utils.status.UserStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "\"user\"")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;// Nombre's

    private String lastname; // Apellidos

    private String email; // Correo (viene siendo el username)

    private String password; // Contraseña

    private String phone; // Numero Telefonico

    private String street;// Calle

    private String exteriorNumber;// Número exterior

    private String interiorNumber;// Número interior (puede ser null)

    private String neighborhood;// Colonia

    private String municipality; // Municipio

    private String state; // Estado

    private Date hireDate; // Fecha de contratación

    private Double Salary; // Salario

    private String employeeCode; // Clave unico del sistema

    @Enumerated(EnumType.STRING)
    private UserStatus status; // Status

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AuditLogEntity> logs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<InventoryMovementsEntity> movements;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PurchaseEntity> purchases;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SaleEntity> sales;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Double getSalary() {
        return Salary;
    }

    public void setSalary(Double salary) {
        Salary = salary;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
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

    public List<AuditLogEntity> getLogs() {
        return logs;
    }

    public void setLogs(List<AuditLogEntity> logs) {
        this.logs = logs;
    }

    public List<InventoryMovementsEntity> getMovements() {
        return movements;
    }

    public void setMovements(List<InventoryMovementsEntity> movements) {
        this.movements = movements;
    }

    public List<PurchaseEntity> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseEntity> purchases) {
        this.purchases = purchases;
    }
}
