package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.dto.SupplierDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.persistence.entity.SupplierEntity;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

public interface SupplierService {

    ApiResponseDto executeCreateSupplier(SupplierDto supplierDto, BindingResult bindingResult) throws BadRequestException;
    ApiResponseDto executeUpdateSupplier(String supplierId, SupplierDto supplierDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeGetSupplierList(int page, int size, String supplierId, String status) throws NotFoundException;
    ApiResponseDto executeGetSupplierListBySelect() throws NotFoundException;
    ApiResponseDto executeGetSupplier(String supplierId) throws NotFoundException;
    ApiResponseDto executeDisabledSupplier(String supplierId, DisabledRegisterDto disabledRegisterDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeImportSupplier(MultipartFile file) throws BadRequestException;
    SupplierEntity getSupplierById(String supplierId) throws NotFoundException;
}
