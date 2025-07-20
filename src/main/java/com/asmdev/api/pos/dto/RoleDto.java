package com.asmdev.api.pos.dto;

import com.asmdev.api.pos.utils.status.ModuleSystem;
import com.asmdev.api.pos.utils.status.NamePermissions;
import com.asmdev.api.pos.utils.status.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RoleDto implements Serializable {

    private String id;

    @NotNull(message = "El nombre del rol es obligatorio")
    @NotBlank(message = "El nombre del rol no puede ser vacio")
    private String name;

    @NotNull(message = "La descripción del rol es obligatorio")
    @NotBlank(message = "La descripción del rol es obligatorio")
    private String description;

    @NotNull(message = "El estado es obligatorio")
    private Status status;

    private List<PermissionDto> permissions;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<PermissionDto> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDto> permissions) {
        this.permissions = permissions;
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
