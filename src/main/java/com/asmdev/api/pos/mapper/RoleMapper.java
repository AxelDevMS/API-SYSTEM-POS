package com.asmdev.api.pos.mapper;


import com.asmdev.api.pos.dto.RoleDto;
import com.asmdev.api.pos.persistence.entity.RoleEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    @Autowired
    private ModelMapper modelMapper;

    public RoleDto convertToDto(RoleEntity roleEntity){
        return this.modelMapper.map(roleEntity, RoleDto.class);
    }

    public RoleEntity convertToEntity(RoleDto roleDto){
        return this.modelMapper.map(roleDto, RoleEntity.class);
    }

}
