package com.asmdev.api.pos.mapper;

import com.asmdev.api.pos.dto.CashMovementsDto;
import com.asmdev.api.pos.dto.CashRegister.CashRegisterDto;
import com.asmdev.api.pos.dto.RoleDto;
import com.asmdev.api.pos.dto.UserDto;
import com.asmdev.api.pos.persistence.entity.CashMovementsEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CashMovementsMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CashMovementsDto convertToDto(CashMovementsEntity cashMovementsEntity){
        CashMovementsDto dto =  this.modelMapper.map(cashMovementsEntity, CashMovementsDto.class);
        if (cashMovementsEntity != null){
            CashRegisterDto cashRegisterDto = new CashRegisterDto();
            UserDto userDto = new UserDto();
            cashRegisterDto.setId(cashMovementsEntity.getId());
            userDto.setId(cashMovementsEntity.getUser().getId());
            dto.setUser(userDto);
            dto.setCashRegister(cashRegisterDto);
        }
        return dto;
    }

    public CashMovementsDto convertToDtoGetMovement(CashMovementsEntity cashMovementsEntity){
        CashMovementsDto dto =  this.modelMapper.map(cashMovementsEntity, CashMovementsDto.class);
        if (cashMovementsEntity != null){
            CashRegisterDto cashRegisterDto = new CashRegisterDto();
            UserDto userDto = new UserDto();
            RoleDto roleDto = new RoleDto();

            cashRegisterDto.setId(cashMovementsEntity.getId());
            cashRegisterDto.setStatus(cashMovementsEntity.getCashRegister().getStatus());
            cashRegisterDto.setNotes(cashMovementsEntity.getCashRegister().getNotes());

            roleDto.setId(cashMovementsEntity.getUser().getRole().getId());
            roleDto.setName(cashMovementsEntity.getUser().getRole().getName());

            userDto.setId(cashMovementsEntity.getUser().getId());
            userDto.setName(cashMovementsEntity.getUser().getName());
            userDto.setLastname(cashMovementsEntity.getUser().getLastname());
            userDto.setRole(roleDto);

            dto.setUser(userDto);
            dto.setCashRegister(cashRegisterDto);
        }
        return dto;
    }




    public CashMovementsEntity convertToEntity(CashMovementsDto cashMovementsDto){
        return this.modelMapper.map(cashMovementsDto, CashMovementsEntity.class);
    }


}
