package com.asmdev.api.pos.controller;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.CashRegister.CashRegisterDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.security.preauthorize.CashRegisterPreAuthorize.*;
import com.asmdev.api.pos.service.CashRegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/pos/cash/register")
public class CashRegisterController {

    @Autowired
    private CashRegisterService cashRegisterService;

    @CanCreateRegisterCash
    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreateCashRegister(
            @Valid @RequestBody CashRegisterDto cashRegisterDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.cashRegisterService.executeCreateCashRegister(cashRegisterDto, bindingResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @CanListRegisterCash
    @GetMapping("/get-alls")
    public ResponseEntity<ApiResponseDto> executeGetCashRegisterList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String cashRegisterId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) throws NotFoundException {
        ApiResponseDto response = this.cashRegisterService.executeGetCashRegisterList(page,size,cashRegisterId,status,startDate,endDate);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CanListRegisterCash
    @GetMapping("/select")
    public ResponseEntity<ApiResponseDto> executeGetCashRegisterListBySelect() throws NotFoundException {
        ApiResponseDto response = this.cashRegisterService.executeGetCashRegisterListBySelect();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CanGetRegisterCash
    @GetMapping("/get/{cashRegisterId}")
    public ResponseEntity<ApiResponseDto> executeGetCashRegister(@PathVariable String cashRegisterId) throws NotFoundException {
        ApiResponseDto response = this.cashRegisterService.executeGetCashRegister(cashRegisterId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CanClosedRegisterCash
    @PatchMapping("/closed/{cashRegisterId}")
    public ResponseEntity<ApiResponseDto> executeCloseCashRegister(
            @PathVariable String cashRegisterId,
            @Valid @RequestBody CashRegisterDto cashRegisterDto,
            BindingResult bindingResult
    ) throws NotFoundException, BadRequestException {
        ApiResponseDto response = this.cashRegisterService.executeCloseCashRegister(cashRegisterId,cashRegisterDto,bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CanUpdateRegisterCash
    @PutMapping("/update/{cashRegisterId}")
    public ResponseEntity<ApiResponseDto> executeUpdateCashRegister(
            @PathVariable String cashRegisterId,
            @Valid @RequestBody CashRegisterDto cashRegisterDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.cashRegisterService.executeUpdateCashRegister(cashRegisterId,cashRegisterDto,bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }




}
