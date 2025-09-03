package com.asmdev.api.pos.security.preauthorize;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public class InventoryMovementPreAuthorize {


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('INVENTORY_MOVEMENT_LIST')")
    public @interface CanListInventoryMovement {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('INVENTORY_MOVEMENT_GET')")
    public @interface CanGetInventoryMovement {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('INVENTORY_MOVEMENT_CREATE')")
    public @interface CanCreateInventoryMovement {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('INVENTORY_MOVEMENT_CANCELED')")
    public @interface CanCanceledInventoryMovement {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('INVENTORY_MOVEMENT_EXPORT')")
    public @interface CanExportInventoryMovement {}


}
