package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.purchase.PurchaseDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.persistence.entity.PurchaseEntity;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

public interface PurchaseService {
    ApiResponseDto executeCreatePurchase(PurchaseDto purchaseDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeGetPurchase(String purchaseId) throws NotFoundException;

    ApiResponseDto executeGetPurchaseList(int page, int size, String supplierId, String purchaseId, String userId, String status, String startDate, String endDate) throws NotFoundException;

    ApiResponseDto executeCancelledPurchase(String purchaseId, @Valid PurchaseDto purchaseDto, BindingResult bindingResult);

    ApiResponseDto executeExportPurchase();

    PurchaseEntity getPurchaseById(String purchaseId) throws NotFoundException;
}
