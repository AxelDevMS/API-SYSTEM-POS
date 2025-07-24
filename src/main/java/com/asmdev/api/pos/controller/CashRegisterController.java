package com.asmdev.api.pos.controller;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.CashRegisterDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
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

    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreateCashRegister(
            @Valid @RequestBody CashRegisterDto cashRegisterDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.cashRegisterService.executeCreateCashRegister(cashRegisterDto, bindingResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/get-alls")
    public ResponseEntity<ApiResponseDto> executeGetCashRegisterList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String cashRegisterId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ){
        ApiResponseDto response = this.cashRegisterService.executeGetCashRegisterList(page,size,cashRegisterId,status,startDate,endDate);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/select")
    public ResponseEntity<ApiResponseDto> executeGetCashRegisterListBySelect() throws NotFoundException {
        ApiResponseDto response = this.cashRegisterService.executeGetCashRegisterListBySelect();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/get/{cashRegisterId}")
    public ResponseEntity<ApiResponseDto> executeGetCashRegister(@PathVariable String cashRegisterId) throws NotFoundException {
        ApiResponseDto response = this.cashRegisterService.executeGetCashRegister(cashRegisterId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/closed/{cashRegisterId}")
    public ResponseEntity<ApiResponseDto> executeModifiedStatus(
            @PathVariable String cashRegisterId,
            @Valid @RequestBody DisabledRegisterDto disabledRegisterDto,
            BindingResult bindingResult
    ) throws NotFoundException, BadRequestException {
        ApiResponseDto response = this.cashRegisterService.executeModifiedStatus(cashRegisterId,disabledRegisterDto,bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/udpate/{cashRegisterId}")
    public ResponseEntity<ApiResponseDto> executeUpdateCashRegister(
            @PathVariable String cashRegisterId,
            @Valid @RequestBody CashRegisterDto cashRegisterDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.cashRegisterService.executeUpdateCashRegister(cashRegisterId,cashRegisterDto,bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }




}
