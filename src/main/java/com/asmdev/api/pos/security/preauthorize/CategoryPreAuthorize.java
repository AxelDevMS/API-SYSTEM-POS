package com.asmdev.api.pos.security.preauthorize;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



public class CategoryPreAuthorize {


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CATEGORY_LIST')")
    public @interface CanListCategory {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CATEGORY_GET')")
    public @interface CanGetCategory {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    public @interface CanCreateCategory {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    public @interface CanUpdateCategory {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    public @interface CanDeletedCategory {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CATEGORY_IMPORT')")
    public @interface CanImportCategory {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CATEGORY_EXPORT')")
    public @interface CanExportCategory {}


}
