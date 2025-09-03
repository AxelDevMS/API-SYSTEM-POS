package com.asmdev.api.pos.security;

import com.asmdev.api.pos.persistence.entity.PermissionEntity;
import com.asmdev.api.pos.persistence.entity.RoleEntity;
import com.asmdev.api.pos.persistence.entity.UserEntity;
import com.asmdev.api.pos.utils.status.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        //agregar roles
        RoleEntity roleEntity = this.userEntity.getRole();
        authorities.add(new SimpleGrantedAuthority(roleEntity.getName()));
        for (PermissionEntity permission: roleEntity.getPermissions() ){
            authorities.add(new SimpleGrantedAuthority(String.valueOf(permission.getName())));
        }

        return authorities;

    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.userEntity.getStatus().equals(UserStatus.ACTIVE);
    }

    public UserEntity getUser(){
        return userEntity;
    }
}
