package com.asmdev.api.pos.dto.authentication;

import java.io.Serializable;
import java.util.List;

public class JwtResponseDto implements Serializable {

    private String token;
    private String type = "Bearer";
    private String id;
    private String email;
    private String role; // Cambiado de List<String> a String
    private List<String> permissions;

    public JwtResponseDto(String token, String type, String id, String email,
                          String role, List<String> permissions) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.email = email;
        this.role = role;
        this.permissions = permissions;
    }


    public JwtResponseDto(){}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
