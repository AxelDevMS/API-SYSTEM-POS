package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.*;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.CustomerMapper;
import com.asmdev.api.pos.persistence.entity.CustomerEntity;
import com.asmdev.api.pos.persistence.repository.CustomerRepository;
import com.asmdev.api.pos.persistence.specification.SpecificationCustomer;
import com.asmdev.api.pos.service.CustomerService;
import com.asmdev.api.pos.utils.status.Status;
import com.asmdev.api.pos.utils.validations.ValidateInputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ValidateInputs validateInputs;

    @Override
    public ApiResponseDto executeCreateCustomer(CustomerDto customerDto, BindingResult bindingResult) throws BadRequestException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        CustomerEntity customerSave = this.customerRepository.save(this.customerMapper.convertToEntity(customerDto));
        return new ApiResponseDto(HttpStatus.CREATED.value(), "El cliente se ha creado exitosamente", this.customerMapper.convertToDto(customerSave));
    }

    @Override
    public ApiResponseDto executeUpdateCustomer(String customerId, CustomerDto customerDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        CustomerEntity customer = this.getCustomerById(customerId);
        customer.setName(customerDto.getName());
        customer.setLastname(customerDto.getLastname());
        customer.setPhone(customerDto.getPhone());
        customer.setStatus(customerDto.getStatus());
        customer = this.customerRepository.save(customer);
        return new ApiResponseDto(HttpStatus.OK.value(),"El cliente se actualizo exitosamente", this.customerMapper.convertToDto(customer));
    }

    @Override
    public ApiResponseDto executeGetCustomerListBySelect() throws NotFoundException {
        List<CustomerEntity> customerListBD = this.customerRepository.findAll();
        if (customerListBD.isEmpty())
            throw new NotFoundException("No hay clientes en el sistema");

        List<CustomerDto> customerList = customerListBD.stream().map(customerMapper::convertToDto).toList();
        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de clientes", customerList);
    }

    @Override
    public ApiResponseDto executeGetCustomerList(int page, int size, String customerId, String status) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Specification<CustomerEntity> filter = SpecificationCustomer.withFilter(customerId, status);
        Page<CustomerEntity> customerListBD = this.customerRepository.findAll(filter,pageable);

        if (customerListBD.isEmpty())
            throw new NotFoundException("No hay clientes en el sistema");

        List<CustomerDto> customerList = customerListBD.getContent().stream().map(customerMapper::convertToDto).toList();
        ListDataPaginationDto pagination = new ListDataPaginationDto();
        pagination.setData(Collections.singletonList(customerList));
        pagination.setTotalElements((int) customerListBD.getTotalElements());
        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de clientes", pagination);
    }

    @Override
    public ApiResponseDto executeGetCustomer(String customerId) throws NotFoundException {
        CustomerEntity customer = this.getCustomerById(customerId);
        return new ApiResponseDto(HttpStatus.OK.value(), "Información del cliente", this.customerMapper.convertToDto(customer));
    }

    @Override
    public ApiResponseDto disabledCustomer(String customerId, DisabledRegisterDto disabledRegisterDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos Inavalidos ", inputValidateList);

        CustomerEntity customerBD = this.getCustomerById(customerId);
        if (Status.valueOf(disabledRegisterDto.getStatus()).equals(customerBD.getStatus()))
            throw new BadRequestException("El cliente ya tiene asignado el mismo estado '" + disabledRegisterDto.getStatus() + "'");

        customerBD.setStatus(Status.valueOf(disabledRegisterDto.getStatus()));
        customerBD = this.customerRepository.save(customerBD);

        return new ApiResponseDto(HttpStatus.OK.value(), "Se actualizo el status del cliente", this.customerMapper.convertToDto(customerBD));
    }

    @Override
    public CustomerEntity getCustomerById(String customerId) throws NotFoundException {
        CustomerEntity customer = this.customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new NotFoundException("No existe este cliente en el sistema");
        return customer;
    }
}
