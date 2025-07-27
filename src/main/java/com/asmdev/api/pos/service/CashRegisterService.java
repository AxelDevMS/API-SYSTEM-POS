package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.CashRegister.CashRegisterDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.persistence.entity.CashMovementsEntity;
import com.asmdev.api.pos.persistence.entity.CashRegisterEntity;
import com.asmdev.api.pos.utils.status.TypeCashMovement;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;

public interface CashRegisterService {
    ApiResponseDto executeCreateCashRegister(@Valid CashRegisterDto cashRegisterDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeGetCashRegisterList(int page, int size, String cashRegisterId, String status, String startDate, String endDate) throws NotFoundException;
    ApiResponseDto executeGetCashRegisterListBySelect() throws NotFoundException;
    ApiResponseDto executeGetCashRegister(String cashRegisterId) throws NotFoundException;
    ApiResponseDto executeCloseCashRegister(String cashRegisterId, CashRegisterDto cashRegisterDto, BindingResult bindingResult) throws NotFoundException, BadRequestException;
    ApiResponseDto executeUpdateCashRegister(String cashRegisterId, CashRegisterDto cashRegisterDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    CashRegisterEntity getCashById(String cashRegisterId) throws NotFoundException;
    CashRegisterEntity updateCurrentAmount(String cashRegisterId, TypeCashMovement movementType, BigDecimal amount) throws NotFoundException, BadRequestException;

    boolean revertCashMovementEffect(String cashRegisterId,CashMovementsEntity cashMovement) throws NotFoundException, BadRequestException;
}
