package com.asmdev.api.pos.mapper;

import com.asmdev.api.pos.dto.ProductDto;
import com.asmdev.api.pos.dto.SupplierDto;
import com.asmdev.api.pos.dto.UserDto;
import com.asmdev.api.pos.dto.purchase.ItemPurchaseDto;
import com.asmdev.api.pos.dto.purchase.PurchaseDto;
import com.asmdev.api.pos.persistence.entity.PurchaseEntity;
import com.asmdev.api.pos.persistence.entity.PurchaseItemsEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PurchaseMapper {

    @Autowired
    private ModelMapper modelMapper;

    public PurchaseDto convertToDto(PurchaseEntity purchaseEntity){
        PurchaseDto dto = this.modelMapper.map(purchaseEntity, PurchaseDto.class);
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(purchaseEntity.getSupplier().getId());
        supplierDto.setName(purchaseEntity.getSupplier().getName());

        UserDto userDto = new UserDto();
        userDto.setId(purchaseEntity.getUser().getId());
        userDto.setName(purchaseEntity.getUser().getName());

        List<ItemPurchaseDto> items = new ArrayList<>();
        for (PurchaseItemsEntity item : purchaseEntity.getItems()){

            ItemPurchaseDto itemPurchaseDto = new ItemPurchaseDto();

            ProductDto productDto = new ProductDto();
            productDto.setId(item.getProduct().getId());
            productDto.setName(item.getProduct().getName());
            productDto.setStock(item.getProduct().getStock());
            productDto.setMinimumStock(item.getProduct().getMinimumStock());

            itemPurchaseDto.setProduct(productDto);
            itemPurchaseDto.setQuantity(item.getQuantity());
            itemPurchaseDto.setUnitPrice(item.getUnitPrice());
            itemPurchaseDto.setTotal(item.getTotal());
            item.setCreatedAt(item.getCreatedAt());

            items.add(itemPurchaseDto);
        }
        dto.setSupplier(supplierDto);
        dto.setUser(userDto);
        dto.setItems(items);

        return dto;
    }

    public PurchaseEntity convertToEntity(PurchaseDto purchaseDto){
        return this.modelMapper.map(purchaseDto, PurchaseEntity.class);
    }

    public ItemPurchaseDto convertToDtoItemsPurchase(PurchaseItemsEntity purchaseItemsEntity){
        return this.modelMapper.map(purchaseItemsEntity, ItemPurchaseDto.class);
    }

    public PurchaseItemsEntity convertToEntityItemsPurchase(ItemPurchaseDto itemPurchaseDto){
        return this.modelMapper.map(itemPurchaseDto, PurchaseItemsEntity.class);
    }
}
