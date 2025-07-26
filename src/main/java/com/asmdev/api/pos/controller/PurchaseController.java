package com.asmdev.api.pos.controller;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.purchase.PurchaseDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pos/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;


    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreatePurchase(
            @Valid @RequestBody PurchaseDto purchaseDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.purchaseService.executeCreatePurchase(purchaseDto, bindingResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get/{purchaseId}")
    public ResponseEntity<ApiResponseDto> executeGetPurchase(@PathVariable String purchaseId){
        ApiResponseDto response = this.purchaseService.executeGetPurchase(purchaseId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
