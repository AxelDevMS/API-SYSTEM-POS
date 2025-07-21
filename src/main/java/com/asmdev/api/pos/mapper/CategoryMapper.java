package com.asmdev.api.pos.mapper;

import com.asmdev.api.pos.dto.CategoryDto;
import com.asmdev.api.pos.persistence.entity.CategoryEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CategoryDto convertToDto(CategoryEntity categoryEntity){
        return this.modelMapper.map(categoryEntity, CategoryDto.class);
    }

    public CategoryEntity convertToEntity(CategoryDto categoryDto){
        return this.modelMapper.map(categoryDto, CategoryEntity.class);
    }

}
