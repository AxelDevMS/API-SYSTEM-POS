package com.asmdev.api.pos.dto;

import com.asmdev.api.pos.utils.status.ModuleSystem;
import com.asmdev.api.pos.utils.status.NamePermissions;
import com.asmdev.api.pos.utils.status.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

public class PermissionDto implements Serializable {

    private String id;

    @NotNull(message = "El nombre del permiso es obligatorio")
    private NamePermissions name;

    @NotNull(message = "El módulo es obligatorio")
    private ModuleSystem module;

    @NotNull(message = "El estado es obligatorio")
    private Status status;

    @NotNull(message = "La descripción del permiso es obligatorio")
    @NotBlank(message = "La descripción del permiso es obligatorio")
    private String description;

    private Date createdAt;

    private Date updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NamePermissions getName() {
        return name;
    }

    public void setName(NamePermissions name) {
        this.name = name;
    }

    public ModuleSystem getModule() {
        return module;
    }

    public void setModule(ModuleSystem module) {
        this.module = module;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
