package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.CashMovementsDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

public interface CashMovementsService {
    ApiResponseDto executeCreateCashMovement(@Valid CashMovementsDto cashMovementsDto, BindingResult bindingResult) throws NotFoundException, BadRequestException;
    ApiResponseDto executeGetCashMovementList(int page, int size, String cashRegisterId, String status, String date);
    ApiResponseDto executeGetCashMovement(String cashRegisterId);
    ApiResponseDto executeUpdateMovement(String cashRegisterId, @Valid CashMovementsDto cashMovementsDto, BindingResult bindingResult);
    ApiResponseDto executeDisabledMovement(String cashRegisterId, @Valid DisabledRegisterDto disabledRegisterDto, BindingResult bindingResult);
}
