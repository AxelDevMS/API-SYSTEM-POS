package com.asmdev.api.pos.security.preauthorize;


import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public class UserPreAuthorize {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('USER_LIST')")
    public @interface CanListUsers {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('USER_GET')")
    public @interface CanGetUser {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public @interface CanCreateUser {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public @interface CanUpdateUser {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public @interface CanDeleteUser {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('USER_RESTORE')")
    public @interface CanRestoreUser {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('ROLE_DELETE') or hasAuthority('ROLE_RESTORE')")
    public @interface CanToggleRoleStatus {}


}
