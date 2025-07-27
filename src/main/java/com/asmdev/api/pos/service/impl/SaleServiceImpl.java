package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.*;
import com.asmdev.api.pos.dto.CashRegister.CashMovementsDto;
import com.asmdev.api.pos.dto.Sale.SaleDto;
import com.asmdev.api.pos.dto.Sale.SaleItemDto;
import com.asmdev.api.pos.dto.purchase.PurchaseDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.SaleMapper;
import com.asmdev.api.pos.persistence.entity.*;
import com.asmdev.api.pos.persistence.repository.CustomerRepository;
import com.asmdev.api.pos.persistence.repository.SaleItemRepository;
import com.asmdev.api.pos.persistence.repository.SaleRepository;
import com.asmdev.api.pos.persistence.specification.SpecificationSale;
import com.asmdev.api.pos.service.*;
import com.asmdev.api.pos.utils.method.InventoryMovementType;
import com.asmdev.api.pos.utils.method.PaymentMethod;
import com.asmdev.api.pos.utils.status.CashMovementsStatus;
import com.asmdev.api.pos.utils.status.NamePermissions;
import com.asmdev.api.pos.utils.status.PurchaseStatus;
import com.asmdev.api.pos.utils.status.TypeCashMovement;
import com.asmdev.api.pos.utils.validations.ValidateInputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private ValidateInputs validateInputs;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryMovementService inventoryMovementService;

    @Autowired
    private CashMovementsService cashMovementsService;

    @Autowired
    private UserService userService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponseDto executeCreateSale(SaleDto saleDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {

        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        CustomerEntity customer = this.customerRepository.findById(saleDto.getCustomer().getId()).orElse(null);
        String customerName = "" ;
        if (customer == null) {
            customerName = null;
        }else{
            customerName = customer.getName();
        }

        UserEntity user = this.userService.getUserById(saleDto.getUser().getId());

        BigDecimal total = saleDto.getItems().stream()
                .map(product -> product.getUnitPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        SaleEntity saleEntity = this.saleMapper.convertToEntity(saleDto);
        saleEntity.setUser(user);
        saleEntity.setCustomer(customer);
        saleEntity.setTotal(total);
        saleEntity.setStatus(PurchaseStatus.COMPLETED);
        saleEntity.setCreatedAt(new Date());
        saleEntity.setItems(new ArrayList<>());

        saleEntity = this.saleRepository.save(saleEntity);
        List<SaleItemEntity> items = this.itemSales(saleEntity,saleDto.getItems(),user.getId());
        saleEntity.setItems(items);

        saleEntity = this.saleRepository.save(saleEntity);
        saleDto.setId(saleEntity.getId());
        this.createCashMovements(saleDto,customerName,total);

        return new ApiResponseDto(HttpStatus.CREATED.value(),"Se registro la venta exitosamente", this.saleMapper.convertToDto(saleEntity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponseDto executeCancelledSale(String saleId, SaleDto saleDto, BindingResult bindingResult) throws NotFoundException, BadRequestException {

        SaleEntity sale = this.getSaleById(saleId);
        if (PurchaseStatus.CANCELED.equals(sale.getStatus()))
            throw new BadRequestException("Esta venta ya se encuentra cancelada");

        CustomerEntity customer = this.customerRepository.findById(saleDto.getCustomer().getId()).orElse(null);
        String customerName = "" ;
        if (customer == null) {
            customerName = null;
        }else{
            customerName = customer.getName();
        }

        UserEntity user = this.userService.getUserById(saleDto.getUser().getId());
        boolean isPermissionCanceled = user.getRole().getPermissions().stream().anyMatch(permission -> permission.getName().equals(NamePermissions.SALE_CANCEL));
        if (!isPermissionCanceled)
            throw new BadRequestException("No tienes permisos para cancelar esta venta");

        sale.setStatus(PurchaseStatus.CANCELED);
        sale.setCancelledUser(user.getId());
        sale.setCancelledAt(new Date());
        sale = this.saleRepository.save(sale);

        List<SaleItemDto> saleItemDtos = sale.getItems().stream().map(saleMapper::convertToDtoSaleItemList).toList();
        this.canceledCashMovementsSale(saleDto, saleId);
        this.itemSales(sale, saleItemDtos, user.getId());

        return new ApiResponseDto(HttpStatus.OK.value(), "Se cancelo la compra exitosamente", this.saleMapper.convertToDto(sale));
    }

    private void canceledCashMovementsSale(SaleDto saleDto, String saleId) throws NotFoundException, BadRequestException {
        if (PaymentMethod.CASH.equals(saleDto.getPaymentMethod())) {
            CashMovementsEntity cashMovement = this.cashMovementsService.findByReferenceId(saleId);
            CashMovementsDto cashMovementsDto = new CashMovementsDto();
            cashMovementsDto.setStatus(CashMovementsStatus.CANCELED);
            cashMovementsDto.setCancelledUser(saleDto.getUser().getId());
            cashMovementsDto.setCancelledAt(new Date());
            this.cashMovementsService.executeCanceledMovement(cashMovement.getId(),  cashMovementsDto, null);
        }
    }

    private List<SaleItemEntity> itemSales(SaleEntity sale, List<SaleItemDto> productList, String userId) throws NotFoundException, BadRequestException {
        List<SaleItemEntity> itemList = new ArrayList<>();

        for (SaleItemDto item : productList){
            ProductEntity product = this.productService.getProductById(item.getProduct().getId());

            SaleItemEntity saleItemEntity = new SaleItemEntity();
            saleItemEntity.setSale(sale);
            saleItemEntity.setProduct(product);
            saleItemEntity.setQuantity(item.getQuantity());
            saleItemEntity.setUnitPrice(item.getUnitPrice());
            item.setTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

            this.createInventoryMovement(saleItemEntity,userId,product.getId(),product.getName(),sale.getStatus());
            itemList.add(saleItemEntity);
        }
        return itemList;
    }

    private void createInventoryMovement(SaleItemEntity saleItemEntity, String userId, String productId, String nameProduct, PurchaseStatus status) throws BadRequestException, NotFoundException {
        InventoryMovementDto inventoryMovement = new InventoryMovementDto();
        ProductDto product = new ProductDto();
        UserDto userDto = new UserDto();
        product.setId(productId);
        userDto.setId(userId);
        inventoryMovement.setUser(userDto);
        inventoryMovement.setProduct(product);
        inventoryMovement.setQuantity(saleItemEntity.getQuantity());
        switch (status) {
            case COMPLETED -> {
                inventoryMovement.setType(InventoryMovementType.SALE);
                inventoryMovement.setDescription("Movimiento de inventario por venta del producto "+ nameProduct);
            }
            case CANCELED -> {
                inventoryMovement.setType(InventoryMovementType.RETURN_SALE);
                inventoryMovement.setDescription("Anulación de salida por cancelación de la venta del producto "+ nameProduct);
            }
        }
        this.inventoryMovementService.executeCreateInventoryMovement(inventoryMovement,null);

    }

    private void createCashMovements(SaleDto saleDto, String nameCustomer, BigDecimal total) throws NotFoundException, BadRequestException {
        if (PaymentMethod.CASH.equals(saleDto.getPaymentMethod())) {
            CashMovementsDto cashMovementsDto = new CashMovementsDto();
            cashMovementsDto.setCashRegister(saleDto.getCash()); // asegúrate que no sea null
            cashMovementsDto.setAmount(total);
            cashMovementsDto.setUser(saleDto.getUser());
            cashMovementsDto.setReferenceId(saleDto.getId());
            cashMovementsDto.setReferenceType("SALE");
            cashMovementsDto.setType(TypeCashMovement.INCOME);
            if (nameCustomer != null && !nameCustomer.isEmpty()){
                cashMovementsDto.setConcept("Venta al cliente  " + nameCustomer);
            }else{
                cashMovementsDto.setConcept("Venta a cliente no identificado");
            }

            this.cashMovementsService.executeCreateCashMovement(cashMovementsDto, null);
        }
    }

    @Override
    public ApiResponseDto executeExportSale() {
        return null;
    }

    @Override
    public ApiResponseDto executeGetSaleList(int page, int size, String customerId, String saleId, String userId, String status, String startDate, String endDate) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Specification<SaleEntity> filter = SpecificationSale.withFilter(customerId, saleId, userId, status, startDate, endDate);
        Page<SaleEntity> saleListBD = this.saleRepository.findAll(filter,pageable);

        if (saleListBD.isEmpty())
            throw new NotFoundException("No hay ventas en el sistema");

        List<SaleDto> saleDtos = saleListBD.getContent().stream().map(saleMapper::convertToDto).toList();
        ListDataPaginationDto paginationDto =  new ListDataPaginationDto();
        paginationDto.setData(Collections.singletonList(saleDtos));
        paginationDto.setTotalElements((int) saleListBD.getTotalElements());
        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de ventas", paginationDto);
    }

    @Override
    public ApiResponseDto executeGetSale(String saleId) throws NotFoundException {
        SaleEntity sale = this.getSaleById(saleId);
        return new ApiResponseDto(HttpStatus.OK.value(), "Información de la venta", this.saleMapper.convertToDto(sale));
    }

    @Override
    public SaleEntity getSaleById(String saleId) throws NotFoundException {
        SaleEntity sale = this.saleRepository.findById(saleId).orElse(null);
        if (sale == null)
            throw new NotFoundException("No existe esta venta en la base de datos");

        return sale;
    }



}
