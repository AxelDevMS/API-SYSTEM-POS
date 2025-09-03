package com.asmdev.api.pos.security.preauthorize;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class SupplierPreAuthorize {


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SUPPLIER_LIST')")
    public @interface CanListSupplier {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SUPPLIER_GET')")
    public @interface CanGetSupplier{}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SUPPLIER_CREATE')")
    public @interface CanCreatedSupplier {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SUPPLIER_UPDATE')")
    public @interface CanUpdatedSupplier{}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SUPPLIER_DELETE')")
    public @interface CanDeletedSupplier {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SUPPLIER_IMPORT')")
    public @interface CanImportSupplier {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SUPPLIER_EXPORT')")
    public @interface CanExportSupplier {}




}
