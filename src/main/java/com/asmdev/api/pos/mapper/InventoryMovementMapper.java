package com.asmdev.api.pos.mapper;

import com.asmdev.api.pos.dto.CashRegister.CashRegisterDto;
import com.asmdev.api.pos.dto.InventoryMovementDto;
import com.asmdev.api.pos.dto.ProductDto;
import com.asmdev.api.pos.dto.RoleDto;
import com.asmdev.api.pos.dto.UserDto;
import com.asmdev.api.pos.persistence.entity.InventoryMovementsEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryMovementMapper {
    @Autowired
    private ModelMapper modelMapper;

    public InventoryMovementDto convertToDto(InventoryMovementsEntity inventoryMovementsEntity){
        InventoryMovementDto dto = this.modelMapper.map(inventoryMovementsEntity, InventoryMovementDto.class);

        UserDto userDto = new UserDto();
        userDto.setId(inventoryMovementsEntity.getUser().getId());
        userDto.setName(inventoryMovementsEntity.getUser().getName());
        userDto.setLastname(inventoryMovementsEntity.getUser().getLastname());

        RoleDto roleDto = new RoleDto();
        roleDto.setId(inventoryMovementsEntity.getUser().getRole().getId());
        roleDto.setName(inventoryMovementsEntity.getUser().getRole().getName());

        ProductDto productDto = new ProductDto();
        productDto.setId(inventoryMovementsEntity.getProduct().getId());
        productDto.setName(inventoryMovementsEntity.getProduct().getName());
        productDto.setStock(inventoryMovementsEntity.getProduct().getStock());
        productDto.setMinimumStock(inventoryMovementsEntity.getProduct().getMinimumStock());

        userDto.setRole(roleDto);
        dto.setUser(userDto);
        dto.setProduct(productDto);

        return dto;


    }

    public InventoryMovementsEntity convertToEntity(InventoryMovementDto inventoryMovementDto){
        return this.modelMapper.map(inventoryMovementDto, InventoryMovementsEntity.class);
    }
}
