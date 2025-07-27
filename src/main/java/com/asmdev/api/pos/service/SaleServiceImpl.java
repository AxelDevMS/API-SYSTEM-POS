package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.Sale.SaleDto;
import com.asmdev.api.pos.mapper.SaleMapper;
import com.asmdev.api.pos.persistence.repository.SaleItemRepository;
import com.asmdev.api.pos.persistence.repository.SaleRepository;
import com.asmdev.api.pos.utils.validations.ValidateInputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class SaleServiceImpl implements SaleService{

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private ValidateInputs validateInputs;

    @Override
    public ApiResponseDto executeCreateSale(SaleDto saleDto, BindingResult bindingResult) {
        return null;
    }
}
