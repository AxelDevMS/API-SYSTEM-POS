package com.asmdev.api.pos.controller;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.Sale.SaleDto;
import com.asmdev.api.pos.dto.purchase.PurchaseDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.security.preauthorize.SalePreAuthorize.*;
import com.asmdev.api.pos.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pos/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @CanCreatedSale
    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreateSale(
            @Valid @RequestBody SaleDto saleDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.saleService.executeCreateSale(saleDto,bindingResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @CanGetSale
    @GetMapping("/get/{saleId}")
    public ResponseEntity<ApiResponseDto> executeGetSale(@PathVariable String saleId) throws NotFoundException {
        ApiResponseDto response = this.saleService.executeGetSale(saleId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CanListSale
    @GetMapping("/get-alls")
    public ResponseEntity<ApiResponseDto> executeGetSaleList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String saleId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) throws NotFoundException {
        ApiResponseDto response = this.saleService.executeGetSaleList(page,size,customerId,saleId,userId,status,startDate,endDate);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CanCanceledSale
    @PatchMapping("/cancelled/{saleId}")
    public ResponseEntity<ApiResponseDto> executeCancelledSale(
            @PathVariable String saleId,
            @Valid @RequestBody SaleDto saleDto,
            BindingResult bindingResult
    ) throws NotFoundException, BadRequestException {
        ApiResponseDto response = this.saleService.executeCancelledSale(saleId, saleDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CanExportSale
    @GetMapping("/export")
    public ResponseEntity<ApiResponseDto> executeExportSale(){
        ApiResponseDto response = this.saleService.executeExportSale();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
