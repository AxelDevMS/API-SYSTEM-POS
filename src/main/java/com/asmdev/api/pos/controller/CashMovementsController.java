package com.asmdev.api.pos.controller;


import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.CashMovementsDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.service.CashMovementsService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pos/cash/movements")
public class CashMovementsController {

    @Autowired
    private CashMovementsService cashMovementsService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreateCashMovement(
            @Valid @RequestBody CashMovementsDto cashMovementsDto,
            BindingResult bindingResult
    ) throws NotFoundException, BadRequestException {
        ApiResponseDto response = this.cashMovementsService.executeCreateCashMovement(cashMovementsDto,bindingResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get-alls")
    public ResponseEntity<ApiResponseDto> executeGetCashMovementList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String cashRegisterId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) throws NotFoundException {
        ApiResponseDto response = this.cashMovementsService.executeGetCashMovementList(page,size,userId,cashRegisterId,type,status,startDate,endDate);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/get/{cashRegisterId}")
    public ResponseEntity<ApiResponseDto> executeGetCashMovement(@PathVariable String cashRegisterId) throws NotFoundException {
        ApiResponseDto response = this.cashMovementsService.executeGetCashMovement(cashRegisterId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/update/{cashRegisterId}")
    public ResponseEntity<ApiResponseDto> executeUpdateMovement(
            @PathVariable String cashRegisterId,
            @Valid @RequestBody CashMovementsDto cashMovementsDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.cashMovementsService.executeUpdateMovement(cashRegisterId,cashMovementsDto,bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PatchMapping("/disabled/{cashRegisterId}")
    public ResponseEntity<ApiResponseDto> executeCanceledMovement(
            @PathVariable String cashRegisterId,
            @Valid @RequestBody CashMovementsDto cashMovementsDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.cashMovementsService.executeCanceledMovement(cashRegisterId,cashMovementsDto,bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
