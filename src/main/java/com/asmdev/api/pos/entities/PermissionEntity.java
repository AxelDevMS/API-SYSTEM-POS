package com.asmdev.api.pos.entities;


import com.asmdev.api.pos.utils.status.ModuleSystem;
import com.asmdev.api.pos.utils.status.NamePermissions;
import com.asmdev.api.pos.utils.status.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "permission")
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private NamePermissions name;

    @Enumerated(EnumType.STRING)
    private ModuleSystem module;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany(mappedBy = "permissions")
    private List<RoleEntity> roles;

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

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
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
