package com.asmdev.api.pos.mapper;


import com.asmdev.api.pos.dto.CashRegisterDto;
import com.asmdev.api.pos.persistence.entity.CashRegisterEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CashRegisterMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CashRegisterDto convertToDto(CashRegisterEntity categoryEntity){
        return this.modelMapper.map(categoryEntity, CashRegisterDto.class);
    }

    public CashRegisterEntity convertToEntity(CashRegisterDto cashRegisterDto){
        return this.modelMapper.map(cashRegisterDto, CashRegisterEntity.class);
    }
}
