package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.CashMovementsDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.persistence.entity.CashMovementsEntity;
import com.asmdev.api.pos.utils.status.CashMovementsStatus;
import jakarta.validation.Valid;
import org.apache.juli.logging.Log;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;

public interface CashMovementsService {
    ApiResponseDto executeCreateCashMovement(CashMovementsDto cashMovementsDto, BindingResult bindingResult) throws NotFoundException, BadRequestException;
    ApiResponseDto executeGetCashMovementList(int page, int size, String userId, String cashRegisterId, String type, String status, String startDate, String endDate) throws NotFoundException;
    ApiResponseDto executeGetCashMovement(String cashRegisterId) throws NotFoundException;
    ApiResponseDto executeUpdateMovement(String cashRegisterId,CashMovementsDto cashMovementsDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeCanceledMovement(String cashMovementId,CashMovementsDto cashMovementsDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    Long countByCashRegisterIdAndStatus(String cashRegisterId, CashMovementsStatus status);
    CashMovementsEntity findByReferenceId(String referenceId) throws NotFoundException;
}
