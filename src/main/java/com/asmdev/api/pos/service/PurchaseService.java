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
}
