package com.asmdev.api.pos.security;

import com.asmdev.api.pos.persistence.entity.PermissionEntity;
import com.asmdev.api.pos.persistence.entity.RoleEntity;
import com.asmdev.api.pos.persistence.entity.UserEntity;
import com.asmdev.api.pos.utils.status.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final UserEntity user;

    private final List<GrantedAuthority> authorities;

    private final String role; // Rol principal

    private final List<String> permissions; // Lista de permisos


    public CustomUserDetails(UserEntity user) {
        this.user = user;
        this.role = "ROLE_" + user.getRole().getName(); // Solo el nombre del rol

        // Permisos sin el prefijo del rol
        this.permissions = user.getRole().getPermissions().stream()
                .map(permission -> permission.getName().name())
                .collect(Collectors.toList());

        // Authorities para Spring Security (combinación de rol + permisos)
        this.authorities = new ArrayList<>();
        this.authorities.add(new SimpleGrantedAuthority(this.role));
        this.permissions.forEach(permission ->
                this.authorities.add(new SimpleGrantedAuthority(permission))
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != UserStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserStatus.ACTIVE;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getRole() {
        return role;
    }

    public List<String> getPermissions() {
        return permissions;
    }

}
