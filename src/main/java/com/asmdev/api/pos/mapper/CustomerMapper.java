package com.asmdev.api.pos.mapper;


import com.asmdev.api.pos.dto.CustomerDto;
import com.asmdev.api.pos.dto.Sale.SaleDto;
import com.asmdev.api.pos.persistence.entity.CustomerEntity;
import com.asmdev.api.pos.persistence.entity.SaleEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CustomerDto convertToDto(CustomerEntity customerEntity){
        //return this.modelMapper.map(customerEntity, CustomerDto.class);
        CustomerDto dto = this.modelMapper.map(customerEntity, CustomerDto.class);
        List<SaleDto> sales = new ArrayList<>();
        for (SaleEntity item: customerEntity.getSales()){
            SaleDto saleDto = new SaleDto();
            saleDto.setId(item.getId());
            saleDto.setStatus(item.getStatus());
        }
        dto.setSales(sales);

        return dto;
    }

    public CustomerEntity convertToEntity(CustomerDto customerDto){
        return this.modelMapper.map(customerDto, CustomerEntity.class);
    }

}
