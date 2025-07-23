package com.asmdev.api.pos.mapper;


import com.asmdev.api.pos.dto.ProductDto;
import com.asmdev.api.pos.persistence.entity.ProductEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ProductDto convertToDto(ProductEntity productEntity){
        return this.modelMapper.map(productEntity, ProductDto.class);
    }

    public ProductEntity convertToEntity(ProductDto productDto){
        return this.modelMapper.map(productDto, ProductEntity.class);
    }
}
