package com.asmdev.api.pos.mapper;


import com.asmdev.api.pos.dto.CashRegister.CashRegisterDto;
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

    public CashRegisterDto convertToDto(CashRegisterEntity cashRegisterEntity){
        CashRegisterDto dto = this.modelMapper.map(cashRegisterEntity, CashRegisterDto.class);

        if (cashRegisterEntity.getUser() != null){
            UserDto userDto = new UserDto();
            RoleDto roleDto = new RoleDto();
            userDto.setId(cashRegisterEntity.getUser().getId());
            userDto.setName(cashRegisterEntity.getUser().getName());
            userDto.setLastname(cashRegisterEntity.getUser().getLastname());
            roleDto.setId(cashRegisterEntity.getUser().getRole().getId());
            roleDto.setName(cashRegisterEntity.getUser().getRole().getName());
            userDto.setRole(roleDto);
            dto.setUser(userDto);
        }
        return dto;
    }

      public CashRegisterEntity convertToEntity(CashRegisterDto cashRegisterDto){
        return this.modelMapper.map(cashRegisterDto, CashRegisterEntity.class);
    }
}
