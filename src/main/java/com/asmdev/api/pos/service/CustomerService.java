package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.CustomerDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.persistence.entity.CustomerEntity;
import org.springframework.validation.BindingResult;

public interface CustomerService {

    ApiResponseDto executeCreateCustomer(CustomerDto customerDto, BindingResult bindingResult) throws BadRequestException;
    ApiResponseDto executeUpdateCustomer(String customerId, CustomerDto customerDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeGetCustomerListBySelect() throws NotFoundException;
    ApiResponseDto executeGetCustomerList(int page, int size, String customerId, String status) throws NotFoundException;
    ApiResponseDto executeGetCustomer(String customerId) throws NotFoundException;
    ApiResponseDto disabledCustomer(String customerId, DisabledRegisterDto disabledRegisterDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    CustomerEntity getCustomerById(String customerId) throws NotFoundException;
}
