package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.Sale.SaleDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.persistence.entity.SaleEntity;
import org.springframework.validation.BindingResult;

public interface SaleService {
    ApiResponseDto executeCreateSale(SaleDto saleDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeExportSale();
    ApiResponseDto executeCancelledSale(String saleId,SaleDto saleDto, BindingResult bindingResult) throws NotFoundException, BadRequestException;
    ApiResponseDto executeGetSaleList(int page, int size, String customerId, String saleId, String userId, String status, String startDate, String endDate) throws NotFoundException;
    ApiResponseDto executeGetSale(String saleId) throws NotFoundException;
    SaleEntity getSaleById(String saleId) throws NotFoundException;
}
