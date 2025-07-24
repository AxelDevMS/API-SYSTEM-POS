package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.CashRegisterDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.dto.ValidateInputDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.CashRegisterMapper;
import com.asmdev.api.pos.persistence.entity.CashRegisterEntity;
import com.asmdev.api.pos.persistence.entity.UserEntity;
import com.asmdev.api.pos.persistence.repository.CashRegisterRepository;
import com.asmdev.api.pos.service.CashRegisterService;
import com.asmdev.api.pos.service.UserService;
import com.asmdev.api.pos.utils.status.CashRegisterStatus;
import com.asmdev.api.pos.utils.status.UserStatus;
import com.asmdev.api.pos.utils.validations.ValidateInputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.List;

@Service
public class CashRegisterServiceImpl implements CashRegisterService {

    @Autowired
    private CashRegisterRepository cashRegisterRepository;

    @Autowired
    private CashRegisterMapper cashRegisterMapper;

    @Autowired
    private ValidateInputs validateInputs;

    @Autowired
    private UserService userService;


    @Override
    public ApiResponseDto executeCreateCashRegister(CashRegisterDto cashRegisterDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        UserEntity user = this.userService.getUserById(cashRegisterDto.getUser().getId());
        if (!user.getStatus().equals(UserStatus.ACTIVE))
            throw new BadRequestException("Este usuario no puede apertura una caja ya que su cuenta no esta activa");

        boolean userHasOpenCash = this.cashRegisterRepository.existsByUserIdAndStatus(user.getId(),CashRegisterStatus.OPEN);
        if (userHasOpenCash)
            throw new BadRequestException("El usaurio ya tiene un caja abierta");

        CashRegisterEntity cashRegister = this.cashRegisterMapper.convertToEntity(cashRegisterDto);
        cashRegister.setUser(user);
        cashRegister.setStatus(CashRegisterStatus.OPEN);
        cashRegister.setOpenedAt(new Date());
        cashRegister.setExpectedAmount(cashRegisterDto.getOpeningAmount());
        cashRegister = this.cashRegisterRepository.save(cashRegister);

        return new ApiResponseDto(HttpStatus.CREATED.value(),"Se ha aperturado la caja", this.cashRegisterMapper.convertToDto(cashRegister));
    }

    @Override
    public ApiResponseDto executeGetCashRegisterList(int page, int size, String cashRegisterId, String status, String startDate, String endDate) {
        return null;
    }

    @Override
    public ApiResponseDto executeGetCashRegisterListBySelect() throws NotFoundException {
        List<CashRegisterEntity> cashRegisterListBD = this.cashRegisterRepository.findAll();
        if (cashRegisterListBD.isEmpty())
            throw new NotFoundException("No tienes cajas en el sistema");

        List<CashRegisterDto> cashRegisterList = cashRegisterListBD.stream().map(cashRegisterMapper::convertToDto).toList();
        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de cajas aperturadas", cashRegisterList);
    }

    @Override
    public ApiResponseDto executeGetCashRegister(String cashRegisterId) throws NotFoundException {
        CashRegisterEntity cashRegister = this.getCashById(cashRegisterId);
        return new ApiResponseDto(HttpStatus.OK.value(),"Información de la caja", this.cashRegisterMapper.convertToDto(cashRegister));
    }

    @Override
    public ApiResponseDto executeModifiedStatus(String cashRegisterId, DisabledRegisterDto disabledRegisterDto, BindingResult bindingResult) throws NotFoundException, BadRequestException {
        CashRegisterEntity cashRegister = this.getCashById(cashRegisterId);
        if (cashRegister.getStatus().equals(CashRegisterStatus.valueOf(disabledRegisterDto.getStatus())))
            throw new BadRequestException("La caja ya tiene este mismo estado "+disabledRegisterDto.getStatus());

        cashRegister.setStatus(CashRegisterStatus.valueOf(disabledRegisterDto.getStatus()));
        cashRegister = this.cashRegisterRepository.save(cashRegister);
        return new ApiResponseDto(HttpStatus.OK.value(),"Se actualizo la caja exitosamente", this.cashRegisterMapper.convertToDto(cashRegister));
    }

    @Override
    public ApiResponseDto executeUpdateCashRegister(String cashRegisterId, CashRegisterDto cashRegisterDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        UserEntity user = this.userService.getUserById(cashRegisterDto.getUser().getId());
        if (!user.getStatus().equals(UserStatus.ACTIVE))
            throw new BadRequestException("Este usuario no puede apertura una caja ya que su cuenta no esta activa");

        CashRegisterEntity cashRegister = this.getCashById(cashRegisterId);
        cashRegister.setUser(user);
        cashRegister.setOpeningAmount(cashRegisterDto.getOpeningAmount());
        cashRegister.setClosingAmount(cashRegisterDto.getClosingAmount());
        cashRegister.setExpectedAmount(cashRegisterDto.getExpectedAmount());
        cashRegister.setOpenedAt(cashRegisterDto.getOpenedAt());
        cashRegister.setClosedAt(cashRegisterDto.getClosedAt());
        cashRegister.setStatus(cashRegisterDto.getStatus());
        cashRegister.setNotes(cashRegisterDto.getNotes());

        cashRegister = this.cashRegisterRepository.save(cashRegister);
        return new ApiResponseDto(HttpStatus.OK.value(), "Se actualizo información del la caja de forma exitosa",this.cashRegisterMapper.convertToDto(cashRegister));
    }

    @Override
    public CashRegisterEntity getCashById(String cashRegisterId) throws NotFoundException {
        CashRegisterEntity cashRegister = this.cashRegisterRepository.findById(cashRegisterId).orElse(null);
        if (cashRegister == null)
            throw new NotFoundException("No existe que esta caja se haya aperturado en el sistema");

        return cashRegister;
    }
}
