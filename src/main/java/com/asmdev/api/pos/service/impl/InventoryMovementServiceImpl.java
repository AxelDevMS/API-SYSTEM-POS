package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.InventoryMovementDto;
import com.asmdev.api.pos.dto.ListDataPaginationDto;
import com.asmdev.api.pos.dto.ValidateInputDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.InventoryMovementMapper;
import com.asmdev.api.pos.mapper.ProductMapper;
import com.asmdev.api.pos.persistence.entity.InventoryMovementsEntity;
import com.asmdev.api.pos.persistence.entity.ProductEntity;
import com.asmdev.api.pos.persistence.entity.UserEntity;
import com.asmdev.api.pos.persistence.repository.InventoryMovementRepository;
import com.asmdev.api.pos.persistence.specification.SpecificationInventoryMovements;
import com.asmdev.api.pos.service.InventoryMovementService;
import com.asmdev.api.pos.service.ProductService;
import com.asmdev.api.pos.service.UserService;
import com.asmdev.api.pos.utils.status.ProductStatus;
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
import java.util.List;

@Service
public class InventoryMovementServiceImpl implements InventoryMovementService {

    @Autowired
    private InventoryMovementRepository inventoryMovementRepository;

    @Autowired
    private InventoryMovementMapper inventoryMovementMapper;

    @Autowired
    private ValidateInputs validateInputs;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;


    @Override
    public ApiResponseDto executeCreateInventoryMovement(InventoryMovementDto inventoryMovementDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        InventoryMovementsEntity inventoryMovement = new InventoryMovementsEntity();
        UserEntity user = this.userService.getUserById(inventoryMovementDto.getUser().getId());

        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        ProductEntity product = this.productService.getProductById(inventoryMovementDto.getProduct().getId());
        if (!product.getStatus().equals(ProductStatus.ACTIVE))
            throw new BadRequestException("No puedes usar este producto ya que no se encuentra activo");

        switch (inventoryMovementDto.getType()){
            case INBOUND:
            case PURCHASE:
            case RETURN_SALE:
            case TRANSFER_IN:
                int addStock = product.getStock() + inventoryMovementDto.getQuantity();
                product.setStock(addStock);
                inventoryMovement = this.applyInventoryMovementLogic(inventoryMovementDto,product,user);
                break;

            case OUTBOUND:
            case SALE:
            case RETURN_PURCHASE:
            case TRANSFER_OUT:
            case DAMAGE:
            case THEFT:
                if (product.getStock() < inventoryMovement.getQuantity())
                    throw new BadRequestException("Stock insuficiente para el prodcuto "+product.getName());

                int subTractStock = product.getStock() - inventoryMovementDto.getQuantity();
                product.setStock(subTractStock);
                inventoryMovement = this.applyInventoryMovementLogic(inventoryMovementDto,product,user);
                break;

            case INVENTORY_COUNT:
                product.setStock(inventoryMovementDto.getQuantity());
                inventoryMovement = this.applyInventoryMovementLogic(inventoryMovementDto,product,user);
                break;

        }
        return new ApiResponseDto(HttpStatus.CREATED.value(),"Movimiento de inventiario registrado exitosamente", this.inventoryMovementMapper.convertToDto(inventoryMovement));
    }

    private InventoryMovementsEntity applyInventoryMovementLogic(InventoryMovementDto inventoryMovementDto, ProductEntity product, UserEntity user) throws BadRequestException, NotFoundException {
        InventoryMovementsEntity inventoryMovement;
        this.productService.updateStock(product.getId(),product.getStock());
        inventoryMovement = this.inventoryMovementMapper.convertToEntity(inventoryMovementDto);
        inventoryMovement.setUser(user);
        inventoryMovement.setProduct(product);
        inventoryMovement = this.inventoryMovementRepository.save(inventoryMovement);
        return inventoryMovement;
    }


    @Override
    public ApiResponseDto executeGetInventoryMovementList(int page, int size, String userId, String productId, String type, String startDate, String endDate) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Specification<InventoryMovementsEntity> filter = SpecificationInventoryMovements.withFilter(userId, productId, type, startDate, endDate);
        Page<InventoryMovementsEntity> inventoryMovementListBD = this.inventoryMovementRepository.findAll(filter,pageable);

        if (inventoryMovementListBD.isEmpty())
            throw new NotFoundException("No hay movientos del inventario");

        List<InventoryMovementDto> inventoryMovementDtos = inventoryMovementListBD.getContent().stream().map(inventoryMovementMapper::convertToDto).toList();
        ListDataPaginationDto pagination = new ListDataPaginationDto();
        pagination.setData(Collections.singletonList(inventoryMovementDtos));
        pagination.setTotalElements((int) inventoryMovementListBD.getTotalElements());
        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de movimientos de inventario", pagination);
    }

    @Override
    public ApiResponseDto executeGetInventoryMovement(String inventoryMovementId) throws NotFoundException {
        InventoryMovementsEntity inventoryMovements = this.getInventoryMovementById(inventoryMovementId);
        return new ApiResponseDto(HttpStatus.OK.value(), "Información del movimiento de inventario", this.inventoryMovementMapper.convertToDto(inventoryMovements));
    }

    @Override
    public ApiResponseDto executeDeletedInventoryMovement(String inventoryMovementId) {
        return null;
    }

    @Override
    public InventoryMovementsEntity getInventoryMovementById(String inventoryMovementId) throws NotFoundException {
        InventoryMovementsEntity inventoryMovement = this.inventoryMovementRepository.findById(inventoryMovementId).orElse(null);
        if (inventoryMovement == null)
            throw new NotFoundException("Este movimiento de inventario no existe");

        return inventoryMovement;
    }
}
