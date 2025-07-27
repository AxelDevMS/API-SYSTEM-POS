package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.*;
import com.asmdev.api.pos.dto.CashRegister.CashMovementsDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.CashMovementsMapper;
import com.asmdev.api.pos.persistence.entity.CashMovementsEntity;
import com.asmdev.api.pos.persistence.entity.CashRegisterEntity;
import com.asmdev.api.pos.persistence.repository.CashMovementsRepository;
import com.asmdev.api.pos.persistence.specification.SpecificationCashMovements;
import com.asmdev.api.pos.service.CashMovementsService;
import com.asmdev.api.pos.service.CashRegisterService;
import com.asmdev.api.pos.service.UserService;
import com.asmdev.api.pos.utils.status.CashMovementsStatus;
import com.asmdev.api.pos.utils.status.CashRegisterStatus;
import com.asmdev.api.pos.utils.validations.ValidateInputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.Date;
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
    public ApiResponseDto executeGetCashMovementList(int page, int size, String userId, String cashRegisterId, String type, String status, String startDate, String endDate) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Specification<CashMovementsEntity> filter = SpecificationCashMovements.withFilter(userId, cashRegisterId, type, status, startDate, endDate);
        Page<CashMovementsEntity> cashMovementList = this.cashMovementsRepository.findAll(filter,pageable);

        if (cashMovementList.isEmpty())
            throw new NotFoundException("No haya movimientos en le sistema");

        List<CashMovementsDto> cashMovementsDtoList = cashMovementList.getContent().stream().map(cashMovementsMapper::convertToDto).toList();
        ListDataPaginationDto paginationDto = new ListDataPaginationDto();
        paginationDto.setData(Collections.singletonList(cashMovementsDtoList));
        paginationDto.setTotalElements((int) cashMovementList.getTotalElements());

        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de movimientos", paginationDto);
    }

    @Override
    public ApiResponseDto executeGetCashMovement(String cashRegisterId) throws NotFoundException {
        CashMovementsEntity movement = this.getMovementById(cashRegisterId);
        return new ApiResponseDto(HttpStatus.OK.value(), "Información del movimiento", this.cashMovementsMapper.convertToDtoGetMovement(movement));
    }

    @Override
    public ApiResponseDto executeUpdateMovement(String cashRegisterId, CashMovementsDto cashMovementsDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> validateInputList = this.validateInputs.validateInputs(bindingResult);
        if (!validateInputList.isEmpty())
            throw new BadRequestException("Campos invalidos", validateInputList);

        CashMovementsEntity movement = this.getMovementById(cashRegisterId);
        if (!movement.getStatus().equals(CashMovementsStatus.ACTIVE))
            throw new BadRequestException("El movimiento ya no se encuentra activo");

        CashRegisterEntity cashRegister = this.cashRegisterService.getCashById(movement.getCashRegister().getId());
        if (!cashRegister.getStatus().equals(CashRegisterStatus.OPEN))
            throw new BadRequestException("La caja no esta con estatus abierto ya no puedes modificar el movimiento");

        movement.setConcept(cashMovementsDto.getConcept());
        movement.setAmount(cashMovementsDto.getAmount());
        movement.setType(cashMovementsDto.getType());
        movement = this.cashMovementsRepository.save(movement);

        return new ApiResponseDto(HttpStatus.OK.value(), "Se actualizo el movimiento", this.cashMovementsMapper.convertToDto(movement));
    }

    @Override
    public ApiResponseDto executeCanceledMovement(String cashMovementId,CashMovementsDto cashMovementsDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> validateInputList = this.validateInputs.validateInputs(bindingResult);
        if (!validateInputList.isEmpty())
            throw new BadRequestException("Campos invalidos", validateInputList);

        CashMovementsEntity movement = this.getMovementById(cashMovementId);
        movement.setStatus(cashMovementsDto.getStatus());
        movement.setCancelledUser(cashMovementsDto.getCancelledUser());
        movement.setCancelledAt(new Date());
        movement = this.cashMovementsRepository.save(movement);

        boolean isReverCurrentAmount = this.cashRegisterService.revertCashMovementEffect(movement.getCashRegister().getId(),movement);
        if (!isReverCurrentAmount)
            throw new BadRequestException("Hubo un problema al actualizar la información de la caja");

        return new ApiResponseDto(HttpStatus.OK.value(),"El movimiento cambio a estado eliminado", this.cashMovementsMapper.convertToDto(movement));
    }

    private CashMovementsEntity getMovementById(String movementId) throws NotFoundException {
        CashMovementsEntity movement = this.cashMovementsRepository.findById(movementId).orElse(null);
        if (movement == null)
            throw new NotFoundException("No existe este movimiento en el sistema");

        return movement;
    }

    @Override
    public Long countByCashRegisterIdAndStatus(String cashRegisterId, CashMovementsStatus status) {
        return null;
    }

    @Override
    public CashMovementsEntity findByReferenceId(String referenceId) throws NotFoundException {
        CashMovementsEntity cashMovement = this.cashMovementsRepository.findByReferenceId(referenceId).orElse(null);
        if (referenceId == null)
            throw new NotFoundException("No existe este movimiento con este id de referencia");
        return cashMovement;
    }

}
