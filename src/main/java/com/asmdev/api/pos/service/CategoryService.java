package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.CategoryDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.persistence.entity.CategoryEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService {

    ApiResponseDto executeGetListCategories(int page, int size, String categoryId, String status) throws NotFoundException;
    ApiResponseDto executeGetListCategoriesBySelect() throws NotFoundException;
    ApiResponseDto executeGetCategory(String categoryId) throws NotFoundException;
    ApiResponseDto executeCreateCategory(CategoryDto categoryDto, BindingResult bindingResult) throws BadRequestException;
    ApiResponseDto executeUpdateCategory(String categoryId, CategoryDto categoryDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeDisabledCategory(String categoryId, DisabledRegisterDto disabledDto, BindingResult bindingResult) throws NotFoundException, BadRequestException;
    CategoryEntity getCategoryById(String categoryId) throws NotFoundException;
    ApiResponseDto executeCreateMassiveCategories(MultipartFile file) throws BadRequestException;

}