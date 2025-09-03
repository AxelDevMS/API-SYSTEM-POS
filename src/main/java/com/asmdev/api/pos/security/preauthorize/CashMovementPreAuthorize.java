package com.asmdev.api.pos.security.preauthorize;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public class CashMovementPreAuthorize {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CASH_MOVEMENT_LIST')")
    public @interface CanListMovementsCash {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CASH_MOVEMENT_GET')")
    public @interface CanGetMovementCash {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CASH_MOVEMENT_CREATE')")
    public @interface CanCreateMovementCash {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CASH_MOVEMENT_UPDATE')")
    public @interface CanUpdateMovementCash {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CASH_MOVEMENT_CANCELED')")
    public @interface CanDeleteMovementCash {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CASH_MOVEMENT_EXPORT')")
    public @interface CanExportMovementCash {}


}
