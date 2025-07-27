package com.asmdev.api.pos.controller;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.Sale.SaleDto;
import com.asmdev.api.pos.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pos/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreateSale(
            @Valid @RequestBody SaleDto saleDto,
            BindingResult bindingResult
    ){
        ApiResponseDto response = this.saleService.executeCreateSale(saleDto,bindingResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
