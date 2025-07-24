package com.asmdev.api.pos.mapper;


import com.asmdev.api.pos.dto.CashRegisterDto;
import com.asmdev.api.pos.dto.RoleDto;
import com.asmdev.api.pos.dto.UserDto;
import com.asmdev.api.pos.persistence.entity.CashRegisterEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CashRegisterMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CashRegisterDto convertToDto(CashRegisterEntity categoryEntity){
        CashRegisterDto dto = this.modelMapper.map(categoryEntity, CashRegisterDto.class);

        if (categoryEntity.getUser() != null){
            UserDto userDto = new UserDto();
            RoleDto roleDto = new RoleDto();
            userDto.setId(categoryEntity.getUser().getId());
            userDto.setName(categoryEntity.getUser().getName());
            userDto.setLastname(categoryEntity.getUser().getLastname());
            roleDto.setId(categoryEntity.getUser().getRole().getId());
            roleDto.setName(categoryEntity.getUser().getRole().getName());
            userDto.setRole(roleDto);
            dto.setUser(userDto);
        }
        return dto;
    }

    public CashRegisterEntity convertToEntity(CashRegisterDto cashRegisterDto){
        return this.modelMapper.map(cashRegisterDto, CashRegisterEntity.class);
    }
}
