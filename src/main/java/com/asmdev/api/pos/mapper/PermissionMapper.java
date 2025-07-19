package com.asmdev.api.pos.mapper;


import com.asmdev.api.pos.dto.PermissionDto;
import com.asmdev.api.pos.persistence.entity.PermissionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    @Autowired
    private ModelMapper modelMapper;

    public PermissionDto convertToDto(PermissionEntity permissionEntity){
        return this.modelMapper.map(permissionEntity, PermissionDto.class);
    }

    public PermissionEntity convertToEntity(PermissionDto permissionDto){
        return this.modelMapper.map(permissionDto, PermissionEntity.class);
    }
}
