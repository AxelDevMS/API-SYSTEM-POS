package com.asmdev.api.pos.controller;


import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.InventoryMovementDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.service.InventoryMovementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pos/inventory/movements")
public class InventoryMovementController {

    @Autowired
    private InventoryMovementService inventoryMovementService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreateInventoryMovement(
            @Valid @RequestBody InventoryMovementDto inventoryMovementDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.inventoryMovementService.executeCreateInventoryMovement(inventoryMovementDto, bindingResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get-alls")
    public ResponseEntity<ApiResponseDto> executeGetInventoryMovementList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String productId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) throws NotFoundException {
        ApiResponseDto response = this.inventoryMovementService.executeGetInventoryMovementList(page,size,userId,productId,type,startDate,endDate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get/{inventoryMovementId}")
    public ResponseEntity<ApiResponseDto> executeGetInventoryMovement(@PathVariable String inventoryMovementId ) throws NotFoundException {
        ApiResponseDto response = this.inventoryMovementService.executeGetInventoryMovement(inventoryMovementId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/deleted/{inventoryMovementId}")
    public ResponseEntity<ApiResponseDto> executeDeletedInventoryMovement(@PathVariable String inventoryMovementId ){
        ApiResponseDto response = this.inventoryMovementService.executeDeletedInventoryMovement(inventoryMovementId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
