package com.asmdev.api.pos.mapper;

import com.asmdev.api.pos.dto.SupplierDto;
import com.asmdev.api.pos.persistence.entity.SupplierEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    @Autowired
    private ModelMapper modelMapper;

    public SupplierDto convertToDto(SupplierEntity supplierEntity){
        return this.modelMapper.map(supplierEntity, SupplierDto.class);
    }

    public SupplierEntity convertToEntity(SupplierDto supplierDto){
        return this.modelMapper.map(supplierDto, SupplierEntity.class);
    }
}
