package com.asmdev.api.pos.mapper;

import com.asmdev.api.pos.dto.CashMovementsDto;
import com.asmdev.api.pos.persistence.entity.CashMovementsEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CashMovementsMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CashMovementsDto convertToDto(CashMovementsEntity cashMovementsEntity){
        return this.modelMapper.map(cashMovementsEntity, CashMovementsDto.class);
    }

    public CashMovementsEntity convertToEntity(CashMovementsDto cashMovementsDto){
        return this.modelMapper.map(cashMovementsDto, CashMovementsEntity.class);
    }


}
