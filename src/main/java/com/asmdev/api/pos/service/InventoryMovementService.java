package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.InventoryMovementDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.persistence.entity.InventoryMovementsEntity;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

public interface InventoryMovementService {
    ApiResponseDto executeCreateInventoryMovement(@Valid InventoryMovementDto inventoryMovementDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeGetInventoryMovementList(int page, int size, String userId, String productId, String type, String startDate, String endDate) throws NotFoundException;
    ApiResponseDto executeGetInventoryMovement(String inventoryMovementId) throws NotFoundException;
    ApiResponseDto executeDeletedInventoryMovement(String inventoryMovementId);
    InventoryMovementsEntity getInventoryMovementById(String inventoryMovementId) throws NotFoundException;
}
