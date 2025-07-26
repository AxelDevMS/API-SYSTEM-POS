package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.purchase.PurchaseDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

public interface PurchaseService {
    ApiResponseDto executeCreatePurchase(PurchaseDto purchaseDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeGetPurchase(String purchaseId);

    ApiResponseDto executeGetPurchaseList(int page, int size, String supplierId, String purchaseId, String userId, String status, String startDate, String endDate);

    ApiResponseDto executeCancelledPurchase(String purchaseId, @Valid PurchaseDto purchaseDto, BindingResult bindingResult);

    ApiResponseDto executeExportPurchase();
}
