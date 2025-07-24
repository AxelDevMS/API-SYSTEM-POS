package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.CashRegister.CashRegisterDto;
import com.asmdev.api.pos.dto.CashRegister.TotalMovementsDto;
import com.asmdev.api.pos.dto.ListDataPaginationDto;
import com.asmdev.api.pos.dto.ValidateInputDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.CashMovementsMapper;
import com.asmdev.api.pos.mapper.CashRegisterMapper;
import com.asmdev.api.pos.persistence.entity.CashMovementsEntity;
import com.asmdev.api.pos.persistence.entity.CashRegisterEntity;
import com.asmdev.api.pos.persistence.entity.UserEntity;
import com.asmdev.api.pos.persistence.repository.CashMovementsRepository;
import com.asmdev.api.pos.persistence.repository.CashRegisterRepository;
import com.asmdev.api.pos.persistence.specification.SpecificationCashRegister;
import com.asmdev.api.pos.service.CashRegisterService;
import com.asmdev.api.pos.service.UserService;
import com.asmdev.api.pos.utils.status.CashMovementsStatus;
import com.asmdev.api.pos.utils.status.CashRegisterStatus;
import com.asmdev.api.pos.utils.status.TypeCashMovement;
import com.asmdev.api.pos.utils.status.UserStatus;
import com.asmdev.api.pos.utils.validations.ValidateInputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class CashRegisterServiceImpl implements CashRegisterService {

    @Autowired
    private CashRegisterRepository cashRegisterRepository;

    @Autowired
    private CashRegisterMapper cashRegisterMapper;

    @Autowired
    private CashMovementsMapper cashMovementsMapper;

    @Autowired
    private ValidateInputs validateInputs;

    @Autowired
    private UserService userService;

    @Autowired
    private CashMovementsRepository cashMovementsRepository;


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
            throw new BadRequestException("El usuario ya tiene un caja abierta");

        CashRegisterEntity cashRegister = this.cashRegisterMapper.convertToEntity(cashRegisterDto);
        cashRegister.setUser(user);
        cashRegister.setStatus(CashRegisterStatus.OPEN);
        cashRegister.setOpenedAt(new Date());
        cashRegister.setCurrentAmount(cashRegisterDto.getOpeningAmount());
        cashRegister.setClosingAmount(new BigDecimal("0.0"));
        cashRegister.setExpectedAmount(cashRegisterDto.getOpeningAmount());
        cashRegister = this.cashRegisterRepository.save(cashRegister);

        return new ApiResponseDto(HttpStatus.CREATED.value(),"Se ha aperturado la caja", this.cashRegisterMapper.convertToDto(cashRegister));
    }

    @Override
    public ApiResponseDto executeGetCashRegisterList(int page, int size, String cashRegisterId, String status, String startDate, String endDate) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Specification<CashRegisterEntity> filter = SpecificationCashRegister.withFilter(cashRegisterId, status, startDate, endDate);
        Page<CashRegisterEntity> cashRegisterList = this.cashRegisterRepository.findAll(filter,pageable);

        if (cashRegisterList.isEmpty())
            throw new NotFoundException("No hay cajas registradas en el sistema");

        List<CashRegisterDto> cashRegisterDtos = cashRegisterList.getContent().stream().map(cashRegisterMapper::convertToDto).toList();
        ListDataPaginationDto pagination = new ListDataPaginationDto();
        pagination.setData(Collections.singletonList(cashRegisterDtos));
        pagination.setTotalElements((int) cashRegisterList.getTotalElements());
        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de cajas", pagination);
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
        List<CashMovementsEntity> movementList = this.cashMovementsRepository.findAllByCashRegisterIdAndStatus(cashRegisterId,CashMovementsStatus.ACTIVE);

        BigDecimal totalIncome = movementList.stream()
                .filter(movement -> movement.getType() == TypeCashMovement.INCOME)
                .map(CashMovementsEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = movementList.stream()
                .filter(movement -> movement.getType() == TypeCashMovement.EXPENSE)
                .map(CashMovementsEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSales = movementList.stream()
                .filter(sale -> sale.getType() == TypeCashMovement.INCOME && sale.getConcept().toLowerCase().contains("venta"))
                .map(CashMovementsEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CashRegisterDto cashRegisterDto = this.cashRegisterMapper.convertToDto(cashRegister);
        cashRegisterDto.setMovements(movementList.stream().map(cashMovementsMapper::convertToDto).toList());
        cashRegisterDto.setTotals(new TotalMovementsDto(totalIncome,totalExpense,totalSales));

        return new ApiResponseDto(HttpStatus.OK.value(),"Información de la caja", cashRegisterDto);
    }

    @Override
    public ApiResponseDto executeCloseCashRegister(String cashRegisterId, CashRegisterDto cashRegisterDto, BindingResult bindingResult) throws NotFoundException, BadRequestException {

        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        CashRegisterEntity cashRegister = this.getCashById(cashRegisterId);
        if (!cashRegister.getStatus().equals(CashRegisterStatus.OPEN))
            throw new BadRequestException("No se puede modifcar la información de la caja ya que no se encuentra abierta");

        BigDecimal totalIncome = this.cashMovementsRepository.sumByCashRegisterAndTypeAndStatus(cashRegisterId,TypeCashMovement.INCOME, CashMovementsStatus.ACTIVE);
        BigDecimal totalExpense = this.cashMovementsRepository.sumByCashRegisterAndTypeAndStatus(cashRegisterId,TypeCashMovement.EXPENSE,CashMovementsStatus.ACTIVE);
        BigDecimal difference = cashRegisterDto.getClosingAmount().subtract(cashRegister.getCurrentAmount());

        totalIncome = totalIncome != null ? totalIncome: BigDecimal.ZERO;
        totalExpense = totalExpense != null ? totalExpense: BigDecimal.ZERO;

        BigDecimal expectedAmount = cashRegister.getOpeningAmount().add(totalIncome).subtract(totalExpense);

        cashRegister.setExpectedAmount(expectedAmount);
        cashRegister.setClosingAmount(cashRegisterDto.getClosingAmount());
        cashRegister.setDifference(difference);
        cashRegister.setClosedAt(new Date());
        cashRegister.setNotes(cashRegisterDto.getNotes());
        cashRegister.setStatus(CashRegisterStatus.CLOSED);

        cashRegister = this.cashRegisterRepository.save(cashRegister);
        return new ApiResponseDto(HttpStatus.OK.value(), "Se hizo el corte de caja de forma exitosa", this.cashRegisterMapper.convertToDto(cashRegister));
    }

    @Override
    public ApiResponseDto executeUpdateCashRegister(String cashRegisterId, CashRegisterDto cashRegisterDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        CashRegisterEntity cashRegister = this.getCashById(cashRegisterId);
        if (!cashRegister.getStatus().equals(CashRegisterStatus.OPEN))
            throw new BadRequestException("No se puede modifcar la información de la caja ya que no se encuentra abierta");

        UserEntity user = this.userService.getUserById(cashRegister.getUser().getId());
        if (!user.getStatus().equals(UserStatus.ACTIVE))
            throw new BadRequestException("Este usuario no puede apertura una caja ya que su cuenta no esta activa");

        long countMovements = this.cashMovementsRepository.countByCashRegisterIdAndStatus(cashRegisterId, CashMovementsStatus.ACTIVE);
        if (countMovements > 0)
            throw new BadRequestException("No se puede modificar el monto inicial si ya hay movimientos registrados");

        cashRegister.setUser(user);
        cashRegister.setOpeningAmount(cashRegisterDto.getOpeningAmount());
        cashRegister.setExpectedAmount(cashRegisterDto.getOpeningAmount());
        cashRegister.setCurrentAmount(cashRegisterDto.getOpeningAmount());
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

    @Override
    public CashRegisterEntity updateCurrentAmount(String cashRegisterId, TypeCashMovement movementType, BigDecimal amount) throws NotFoundException, BadRequestException {
        CashRegisterEntity cashRegister = this.getCashById(cashRegisterId);
        BigDecimal amountEdit;

        if (!cashRegister.getStatus().equals(CashRegisterStatus.OPEN))
            throw new BadRequestException("La caja no esta abierta para actualizar los montos");

        if (TypeCashMovement.INCOME.equals(movementType)){
            amountEdit = cashRegister.getCurrentAmount().add(amount);
        }else{
            amountEdit = cashRegister.getCurrentAmount().subtract(amount);
        }
        cashRegister.setCurrentAmount(amountEdit);
        cashRegister = this.cashRegisterRepository.save(cashRegister);
        return cashRegister;
    }
}
