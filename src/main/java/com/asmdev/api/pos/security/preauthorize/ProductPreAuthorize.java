package com.asmdev.api.pos.security.preauthorize;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



public class ProductPreAuthorize {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('PRODUCT_LIST')")
    public @interface CanListProduct {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('PRODUCT_GET')")
    public @interface CanGetProduct {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('PRODUCT_GET_LOW_STOCK')")
    public @interface CanGetLowStockProduct {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    public @interface CanCreatedProduct {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    public @interface CanUpdatedProduct{}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    public @interface CanDeletedProduct {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('PRODUCT_IMPORT')")
    public @interface CanImportProduct {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('PRODUCT_EXPORT')")
    public @interface CanExportProduct {}



}
