package com.asmdev.api.pos.controller;


import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.CustomerDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pos/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreateCustomer(
            @Valid @RequestBody CustomerDto customerDto,
            BindingResult bindingResult
    ) throws BadRequestException {
        ApiResponseDto response = this.customerService.executeCreateCustomer(customerDto, bindingResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<ApiResponseDto> executeUpdateCustomer(
            @PathVariable String customerId,
            @Valid @RequestBody CustomerDto customerDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.customerService.executeUpdateCustomer(customerId, customerDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PatchMapping("/disabled/{customerId}")
    public ResponseEntity<ApiResponseDto> executeDisabledCustomer(
            @PathVariable String customerId,
            @Valid @RequestBody DisabledRegisterDto disabledRegisterDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.customerService.disabledCustomer(customerId, disabledRegisterDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/get-alls")
    public ResponseEntity<ApiResponseDto> executeGetCustomerList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String status
    ) throws NotFoundException {
        ApiResponseDto response = this.customerService.executeGetCustomerList(page, size, customerId, status);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/select")
    public ResponseEntity<ApiResponseDto> executeGetCustomerListBySelect() throws NotFoundException {
        ApiResponseDto response = this.customerService.executeGetCustomerListBySelect();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/get/{customerId}")
    public ResponseEntity<ApiResponseDto> executeGetCustomer(@PathVariable String customerId) throws NotFoundException {
        ApiResponseDto response = this.customerService.executeGetCustomer(customerId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
