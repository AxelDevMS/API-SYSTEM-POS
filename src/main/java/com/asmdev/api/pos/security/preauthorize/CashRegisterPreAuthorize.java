package com.asmdev.api.pos.security.preauthorize;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public class CashRegisterPreAuthorize {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CASH_REGISTER_LIST')")
    public @interface CanListRegisterCash {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CASH_REGISTER_GET')")
    public @interface CanGetRegisterCash {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CASH_REGISTER_CREATE')")
    public @interface CanCreateRegisterCash {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CASH_REGISTER_UPDATE')")
    public @interface CanUpdateRegisterCash {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CASH_REGISTER_CLOSED')")
    public @interface CanClosedRegisterCash {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CASH_REGISTER_EXPORT')")
    public @interface CanExportRegisterCash {}



}
