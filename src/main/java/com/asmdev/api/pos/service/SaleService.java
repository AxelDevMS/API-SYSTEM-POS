package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.Sale.SaleDto;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

public interface SaleService {
    ApiResponseDto executeCreateSale(@Valid SaleDto saleDto, BindingResult bindingResult);
}
