package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.*;
import com.asmdev.api.pos.dto.CashRegister.CashMovementsDto;
import com.asmdev.api.pos.dto.purchase.ItemPurchaseDto;
import com.asmdev.api.pos.dto.purchase.PurchaseDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.PurchaseMapper;
import com.asmdev.api.pos.persistence.entity.*;
import com.asmdev.api.pos.persistence.repository.ItemPurchaseRepository;
import com.asmdev.api.pos.persistence.repository.PurchaseRepository;
import com.asmdev.api.pos.persistence.specification.SpecificationPurchase;
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
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ItemPurchaseRepository itemPurchaseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CashMovementsService cashMovementsService;

    @Autowired
    private InventoryMovementService inventoryMovementService;

    @Autowired
    private ValidateInputs validateInputs;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponseDto executeCreatePurchase(PurchaseDto purchaseDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {

        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos inválidos", inputValidateList);

        UserEntity user = this.userService.getUserById(purchaseDto.getUser().getId());
        SupplierEntity supplier = this.supplierService.getSupplierById(purchaseDto.getSupplier().getId());

        BigDecimal total = purchaseDto.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ✅ PRIMERA MODIFICACIÓN: guardar el PurchaseEntity sin los items primero
        PurchaseEntity purchaseEntity = this.purchaseMapper.convertToEntity(purchaseDto);
        purchaseEntity.setUser(user);
        purchaseEntity.setSupplier(supplier);
        purchaseEntity.setTotal(total);
        purchaseEntity.setStatus(PurchaseStatus.COMPLETED);
        purchaseEntity.setCreatedAt(new Date());
        purchaseEntity.setItems(new ArrayList<>()); // Inicializa la lista para evitar null

        purchaseEntity = this.purchaseRepository.save(purchaseEntity); // Persistir primero

        // ✅ SEGUNDA MODIFICACIÓN: pasar el PurchaseEntity persistido (con ID) a itemPurchaseSave
        List<PurchaseItemsEntity> items = this.itemPurchase(purchaseEntity, purchaseDto.getItems(), user.getId(), supplier.getName());
        purchaseEntity.setItems(items); // Asociar los items

        purchaseEntity = this.purchaseRepository.save(purchaseEntity); // Volver a guardar con items

        purchaseDto.setId(purchaseEntity.getId());
        this.createCashMovements(purchaseDto, supplier.getName(), total);

        return new ApiResponseDto(HttpStatus.CREATED.value(), "Se registró esta compra al inventario", this.purchaseMapper.convertToDto(purchaseEntity));
    }


    private void createCashMovements(PurchaseDto purchaseDto, String nameSupplier, BigDecimal total) throws NotFoundException, BadRequestException {
        if (PaymentMethod.CASH.equals(purchaseDto.getPaymentMethod())) {
            CashMovementsDto cashMovementsDto = new CashMovementsDto();
            cashMovementsDto.setCashRegister(purchaseDto.getCash()); // asegúrate que no sea null
            cashMovementsDto.setType(TypeCashMovement.EXPENSE);
            cashMovementsDto.setConcept("Compra al Proveedor " + nameSupplier);
            cashMovementsDto.setAmount(total);
            cashMovementsDto.setUser(purchaseDto.getUser());
            cashMovementsDto.setReferenceId(purchaseDto.getId());
            cashMovementsDto.setReferenceType("PURCHASE");

            this.cashMovementsService.executeCreateCashMovement(cashMovementsDto, null);
        }
    }

    private List<PurchaseItemsEntity> itemPurchase(PurchaseEntity purchase, List<ItemPurchaseDto> itemsDto, String userId, String supplierName) throws NotFoundException, BadRequestException {
        List<PurchaseItemsEntity> itemList = new ArrayList<>();

        for (ItemPurchaseDto itemDto : itemsDto) {
            ProductEntity product = this.productService.getProductById(itemDto.getProduct().getId());

            PurchaseItemsEntity item = new PurchaseItemsEntity();
            item.setPurchase(purchase); // ✅ TERCERA MODIFICACIÓN: usar la instancia persistida
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(itemDto.getUnitPrice());
            item.setTotal(itemDto.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));

            this.createInventoryMovement(item, userId, product.getId(), supplierName, purchase.getStatus()); // mantiene

            itemList.add(item);
        }

        return itemList;
    }

    private void createInventoryMovement(PurchaseItemsEntity purchaseItemsEntity, String userId, String productId,  String nameSupplier, PurchaseStatus status) throws BadRequestException, NotFoundException {
        InventoryMovementDto inventoryMovement = new InventoryMovementDto();
        ProductDto product = new ProductDto();
        UserDto userDto = new UserDto();
        product.setId(productId);
        userDto.setId(userId);
        inventoryMovement.setUser(userDto);
        inventoryMovement.setProduct(product);
        inventoryMovement.setQuantity(purchaseItemsEntity.getQuantity());
        switch (status) {
            case COMPLETED -> {
                inventoryMovement.setType(InventoryMovementType.PURCHASE);
                inventoryMovement.setDescription("Compra al Proveedor "+ nameSupplier);
            }
            case CANCELED -> {
                inventoryMovement.setType(InventoryMovementType.RETURN_PURCHASE);
                inventoryMovement.setDescription("Cancelación de Compra al Proveedor "+ nameSupplier);
            }
        }
        this.inventoryMovementService.executeCreateInventoryMovement(inventoryMovement,null);

    }

    @Override
    public ApiResponseDto executeGetPurchase(String purchaseId) throws NotFoundException {
        PurchaseEntity purchase = this.getPurchaseById(purchaseId);
        return new ApiResponseDto(HttpStatus.OK.value(), "Información de la compra ", this.purchaseMapper.convertToDto(purchase));
    }

    @Override
    public ApiResponseDto executeGetPurchaseList(int page, int size, String supplierId, String purchaseId, String userId, String status, String startDate, String endDate) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Specification<PurchaseEntity> filter = SpecificationPurchase.withFilter(supplierId, purchaseId, userId, status, startDate, endDate);
        Page<PurchaseEntity> purchaseListBD = this.purchaseRepository.findAll(filter,pageable);

        if (purchaseListBD.isEmpty())
            throw new NotFoundException("No hay compras para alimetar el inventario");

        List<PurchaseDto> purchaseDtos = purchaseListBD.getContent().stream().map(purchaseMapper::convertToDto).toList();
        ListDataPaginationDto paginationDto = new ListDataPaginationDto();
        paginationDto.setData(Collections.singletonList(purchaseDtos));
        paginationDto.setTotalElements((int) purchaseListBD.getTotalElements());
        return new ApiResponseDto(HttpStatus.OK.value(),"Listado de compras", paginationDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponseDto executeCancelledPurchase(String purchaseId, PurchaseDto purchaseDto, BindingResult bindingResult) throws NotFoundException, BadRequestException {

        PurchaseEntity purchase = this.getPurchaseById(purchaseId);
        if (PurchaseStatus.CANCELED.equals(purchase.getStatus()))
            throw new BadRequestException("Esta compra ya se encuentra cancelada");

        SupplierEntity supplier = this.supplierService.getSupplierById(purchaseDto.getSupplier().getId());
        UserEntity user = this.userService.getUserById(purchaseDto.getUser().getId());
        boolean permissionCanceled = user.getRole().getPermissions().stream().anyMatch(permission -> permission.getName().equals(NamePermissions.PURCHASE_CANCELED));
        if (!permissionCanceled)
            throw new BadRequestException("No tienes permisos para cancelar la compra");

        purchase.setStatus(PurchaseStatus.CANCELED);
        purchase.setCancelledUser(user.getId());
        purchase.setCancelledAt(new Date());
        purchase = this.purchaseRepository.save(purchase);

        List<ItemPurchaseDto> items = purchase.getItems().stream().map(purchaseMapper::convertToDtoItemsPurchase).toList();
        this.canceledCashMovements(purchaseDto, purchaseId);
        this.itemPurchase(purchase,items,user.getId(),supplier.getName());

        return new ApiResponseDto(HttpStatus.OK.value(), "Se cancelo la compra exitosamente", this.purchaseMapper.convertToDto(purchase));
    }

    private void canceledCashMovements(PurchaseDto purchaseDto, String purchaseId) throws NotFoundException, BadRequestException {
        if (PaymentMethod.CASH.equals(purchaseDto.getPaymentMethod())) {
            CashMovementsEntity cashMovement = this.cashMovementsService.findByReferenceId(purchaseId);
            CashMovementsDto cashMovementsDto = new CashMovementsDto();
            cashMovementsDto.setStatus(CashMovementsStatus.CANCELED);
            cashMovementsDto.setCancelledUser(purchaseDto.getUser().getId());
            cashMovementsDto.setCancelledAt(new Date());
            this.cashMovementsService.executeCanceledMovement(cashMovement.getId(),  cashMovementsDto, null);
        }
    }

    @Override
    public ApiResponseDto executeExportPurchase() {
        return null;
    }

    @Override
    public PurchaseEntity getPurchaseById(String purchaseId) throws NotFoundException {
        PurchaseEntity purchase = this.purchaseRepository.findById(purchaseId).orElse(null);
        if (purchase == null)
            throw new NotFoundException("No existe esta compra de inventario en el sistema");

        return purchase;
    }
}
