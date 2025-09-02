package com.asmdev.api.pos.mapper;

import com.asmdev.api.pos.dto.CustomerDto;
import com.asmdev.api.pos.dto.ProductDto;
import com.asmdev.api.pos.dto.Sale.SaleDto;
import com.asmdev.api.pos.dto.Sale.SaleItemDto;
import com.asmdev.api.pos.dto.UserDto;
import com.asmdev.api.pos.persistence.entity.SaleEntity;
import com.asmdev.api.pos.persistence.entity.SaleItemEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SaleMapper {

    @Autowired
    private ModelMapper modelMapper;

    public SaleDto convertToDto(SaleEntity saleEntity) {
        SaleDto dto = new SaleDto(); // Evita mapear todo automáticamente

        dto.setId(saleEntity.getId());
        dto.setCreatedAt(saleEntity.getCreatedAt());
        dto.setTotal(saleEntity.getTotal());
        dto.setPaymentMethod(saleEntity.getPaymentMethod());
        dto.setStatus(saleEntity.getStatus());

        // Usuario
        UserDto userDto = new UserDto();
        userDto.setId(saleEntity.getUser().getId());
        userDto.setName(saleEntity.getUser().getName());
        userDto.setLastname(saleEntity.getUser().getLastname());
        dto.setUser(userDto);

        /*CustomerDto customerDto = new CustomerDto();
        customerDto.setId(saleEntity.getCustomer().getId());
        customerDto.setName(saleEntity.getCustomer().getName());
        customerDto.setLastname(saleEntity.getCustomer().getLastname());
        dto.setCustomer(customerDto);*/
        // Ítems
        List<SaleItemDto> items = new ArrayList<>();
        for (SaleItemEntity item : saleEntity.getItems()) {
            SaleItemDto saleItemDto = new SaleItemDto();

            ProductDto productDto = new ProductDto();
            productDto.setId(item.getProduct().getId());
            productDto.setName(item.getProduct().getName());
            productDto.setStock(item.getProduct().getStock());
            productDto.setMinimumStock(item.getProduct().getMinimumStock());

            saleItemDto.setProduct(productDto);
            saleItemDto.setQuantity(item.getQuantity());
            saleItemDto.setUnitPrice(item.getUnitPrice());
            saleItemDto.setTotal(item.getTotal());
            saleItemDto.setCreatedAt(item.getCreatedAt());

            items.add(saleItemDto);
        }

        dto.setItems(items);

        return dto;
    }


    public SaleEntity convertToEntity(SaleDto saleDto){
        return this.modelMapper.map(saleDto, SaleEntity.class);
    }

    public SaleItemDto convertToDtoSaleItemList(SaleItemEntity saleItemEntity){
        return this.modelMapper.map(saleItemEntity, SaleItemDto.class);
    }

    public SaleItemEntity convertToEntitySaleItemList(SaleItemDto saleItemDto){
        return this.modelMapper.map(saleItemDto, SaleItemEntity.class);
    }


}
