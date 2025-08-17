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

        /*SupplierDto dto = this.modelMapper.map(supplierEntity, SupplierDto.class);

        dto.setId(supplierEntity.getId());
        dto.setName(supplierEntity.getName());
        dto.setContactPerson(supplierEntity.getContactPerson());
        dto.setPhone(supplierEntity.getPhone());
        dto.setEmail(supplierEntity.getEmail());
        dto.setStreet(supplierEntity.getStreet());
        dto.setExteriorNumber(supplierEntity.getExteriorNumber());
        dto.setInteriorNumber(supplierEntity.getInteriorNumber());
        dto.setNeighborhood(supplierEntity.getNeighborhood());
        dto.setMunicipality(supplierEntity.getMunicipality());
        dto.setState(supplierEntity.getState());
        dto.setStatus(supplierEntity.getStatus());
        dto.setCreatedAt(supplierEntity.getCreatedAt());
        dto.setUpdatedAt(supplierEntity.getUpdatedAt());

        return dto;*/
    }

    public SupplierEntity convertToEntity(SupplierDto supplierDto){
        return this.modelMapper.map(supplierDto, SupplierEntity.class);
    }
}
