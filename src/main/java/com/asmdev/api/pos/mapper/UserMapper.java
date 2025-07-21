package com.asmdev.api.pos.mapper;

import com.asmdev.api.pos.dto.RoleDto;
import com.asmdev.api.pos.dto.UserDto;
import com.asmdev.api.pos.persistence.entity.RoleEntity;
import com.asmdev.api.pos.persistence.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private ModelMapper modelMapper;

    public UserDto convertToDto(UserEntity userEntity){
        //dto.setRole(this.modelMapper.map(userEntity.getRole(), RoleDto.class));
        return this.modelMapper.map(userEntity, UserDto.class);
    }

    public UserEntity convertToEntity(UserDto userDto){
        //entity.setRole(this.modelMapper.map(userDto.getRole(), RoleEntity.class));
        return this.modelMapper.map(userDto, UserEntity.class);
    }


}
