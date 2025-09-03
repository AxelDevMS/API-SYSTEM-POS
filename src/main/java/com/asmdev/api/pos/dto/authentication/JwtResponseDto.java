package com.asmdev.api.pos.dto.authentication;

import java.util.List;

public class JwtResponseDto {

    private String token;
    private String type = "Bearer";
    private String id;
    private String email;
    private List<String> roles;
    private List<String> permissions;


    public JwtResponseDto(){}

    public JwtResponseDto(String accessToken, String id, String email, List<String> roles, List<String> permissions) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.permissions = permissions;
    }

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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
