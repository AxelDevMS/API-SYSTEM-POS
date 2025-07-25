package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.dto.ProductDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.persistence.entity.ProductEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ApiResponseDto executeImportMassiveProducts(MultipartFile file) throws BadRequestException;
    ApiResponseDto executeCreateProduct(ProductDto productDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeUpdateProduct(String productId, ProductDto productDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeGetProductList(int page, int size, String productId, String categoryId, String status) throws NotFoundException;
    ApiResponseDto executeGetProductListBySelect() throws NotFoundException;
    ApiResponseDto executeGetProduct(String productId) throws NotFoundException;
    ApiResponseDto executeGetLowStockProductList() throws NotFoundException;
    byte[] executeExportProducts() throws BadRequestException;
    ApiResponseDto executeDisabledProduct(String productId, DisabledRegisterDto disabledRegisterDto, BindingResult bindingResult) throws NotFoundException, BadRequestException;
    ProductEntity getProductById(String productId) throws NotFoundException;
    ProductEntity updateStock(String productId, int stock) throws NotFoundException;
}
