package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.CashMovementsDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.dto.ValidateInputDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.CashMovementsMapper;
import com.asmdev.api.pos.persistence.entity.CashMovementsEntity;
import com.asmdev.api.pos.persistence.entity.CashRegisterEntity;
import com.asmdev.api.pos.persistence.repository.CashMovementsRepository;
import com.asmdev.api.pos.service.CashMovementsService;
import com.asmdev.api.pos.service.CashRegisterService;
import com.asmdev.api.pos.service.UserService;
import com.asmdev.api.pos.utils.status.CashMovementsStatus;
import com.asmdev.api.pos.utils.status.CashRegisterStatus;
import com.asmdev.api.pos.utils.validations.ValidateInputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class CashMovementsServiceImpl implements CashMovementsService {

    @Autowired
    private CashMovementsRepository cashMovementsRepository;

    @Autowired
    private CashMovementsMapper cashMovementsMapper;

    @Autowired
    private CashRegisterService cashRegisterService;

    @Autowired
    private UserService userService;

    @Autowired
    private ValidateInputs validateInputs;


    @Override
    public ApiResponseDto executeCreateCashMovement(CashMovementsDto cashMovementsDto, BindingResult bindingResult) throws NotFoundException, BadRequestException {
        List<ValidateInputDto> validateInputList = this.validateInputs.validateInputs(bindingResult);
        if (!validateInputList.isEmpty())
            throw new BadRequestException("Campos invalidos", validateInputList);

        CashRegisterEntity cashRegister = this.cashRegisterService.getCashById(cashMovementsDto.getCashRegister().getId());
        if (!cashRegister.getStatus().equals(CashRegisterStatus.OPEN))
            throw new BadRequestException("La caja no esta con estatus abierto");

        CashMovementsEntity cashMovements = this.cashMovementsMapper.convertToEntity(cashMovementsDto);
        cashRegister = this.cashRegisterService.updateCurrentAmount(cashMovementsDto.getCashRegister().getId(), cashMovementsDto.getType() , cashMovementsDto.getAmount());
        cashMovements.setUser(this.userService.getUserById(cashMovementsDto.getUser().getId()));
        cashMovements.setCashRegister(cashRegister);
        cashMovements.setStatus(CashMovementsStatus.ACTIVE);
        cashMovements = this.cashMovementsRepository.save(cashMovements);

        return new ApiResponseDto(HttpStatus.CREATED.value(),"Se registro el movimiento de forma exitosa", this.cashMovementsMapper.convertToDto(cashMovements));
    }

    @Override
    public ApiResponseDto executeGetCashMovementList(int page, int size, String cashRegisterId, String status, String date) {
        return null;
    }

    @Override
    public ApiResponseDto executeGetCashMovement(String cashRegisterId) {
        return null;
    }

    @Override
    public ApiResponseDto executeUpdateMovement(String cashRegisterId, CashMovementsDto cashMovementsDto, BindingResult bindingResult) {
        return null;
    }

    @Override
    public ApiResponseDto executeDisabledMovement(String cashRegisterId, DisabledRegisterDto disabledRegisterDto, BindingResult bindingResult) {
        return null;
    }


}
