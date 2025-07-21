package com.asmdev.api.pos.service.impl;


import com.asmdev.api.pos.dto.*;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.CategoryMapper;
import com.asmdev.api.pos.persistence.entity.CategoryEntity;
import com.asmdev.api.pos.persistence.repository.CategoryRepository;
import com.asmdev.api.pos.persistence.specification.SpecificationCategory;
import com.asmdev.api.pos.service.CategoryService;
import com.asmdev.api.pos.utils.helper.ExcelHelper;
import com.asmdev.api.pos.utils.status.Status;
import com.asmdev.api.pos.utils.validations.ValidateInputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ValidateInputs validateInputs;

    @Override
    public ApiResponseDto executeGetListCategories(int page, int size, String categoryId, String status) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Specification<CategoryEntity> filter = SpecificationCategory.withFilter(categoryId, status);
        Page<CategoryEntity> categoryListBD = this.categoryRepository.findAll(filter,pageable);

        if (categoryListBD.isEmpty())
            throw new NotFoundException("No hay categorias en el sistema");

        List<CategoryDto> categoryList = categoryListBD.getContent().stream().map(categoryMapper::convertToDto).toList();
        ListDataPaginationDto pagination = new ListDataPaginationDto();
        pagination.setData(Collections.singletonList(categoryList));
        pagination.setTotalElements((int) categoryListBD.getTotalElements());

        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de categorias del sistema", pagination);
    }

    @Override
    public ApiResponseDto executeGetListCategoriesBySelect() throws NotFoundException {
        List<CategoryEntity> categoryListBD = this.categoryRepository.findAll();
        if (categoryListBD.isEmpty())
            throw new NotFoundException("No hay categorias en el sistema");

        List<CategoryDto> categoryList = categoryListBD.stream().map(categoryMapper::convertToDto).toList();
        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de categorias del sistema", categoryList);
    }

    @Override
    public ApiResponseDto executeGetCategory(String categoryId) throws NotFoundException {
        CategoryEntity categoryBD = this.getCategoryById(categoryId);
        return new ApiResponseDto(HttpStatus.OK.value(),"Información de la categoria", this.categoryMapper.convertToDto(categoryBD));
    }

    @Override
    public ApiResponseDto executeCreateCategory(CategoryDto categoryDto, BindingResult bindingResult) throws BadRequestException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", validateInputs);

        CategoryEntity categorySave = this.categoryRepository.save(this.categoryMapper.convertToEntity(categoryDto));
        return new ApiResponseDto(HttpStatus.CREATED.value(),"La categoria se creo exitosamente", this.categoryMapper.convertToDto(categorySave));
    }

    @Override
    public ApiResponseDto executeUpdateCategory(String categoryId, CategoryDto categoryDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", validateInputs);

        CategoryEntity categoryBD = this.getCategoryById(categoryId);
        categoryBD.setName(categoryDto.getName());
        categoryBD.setDescription(categoryDto.getDescription());
        categoryBD.setStatus(categoryDto.getStatus());

        categoryBD = this.categoryRepository.save(categoryBD);
        return new ApiResponseDto(HttpStatus.OK.value(),"Se actualizo la categoria exitosamente", this.categoryMapper.convertToDto(categoryBD));
    }

    @Override
    public ApiResponseDto executeDisabledCategory(String categoryId, DisabledRegisterDto disabledDto, BindingResult bindingResult) throws NotFoundException, BadRequestException {
        CategoryEntity categoryBD = this.getCategoryById(categoryId);
        if (!categoryBD.getStatus().equals(Status.ACTIVE))
            throw new BadRequestException("La categoria ya tiene asignado el estado '" + disabledDto.getStatus() + "'");

        categoryBD.setStatus(Status.valueOf(disabledDto.getStatus()));
        categoryBD = this.categoryRepository.save(categoryBD);

        return new ApiResponseDto(HttpStatus.OK.value(), "La categoria cambio de status exitosamente", this.categoryMapper.convertToDto(categoryBD));
    }

    @Override
    public CategoryEntity getCategoryById(String categoryId) throws NotFoundException {
        CategoryEntity categoryDB = this.categoryRepository.findById(categoryId).orElse(null);
        if (categoryDB == null)
            throw new NotFoundException("No existe la categoria con ID "+ categoryId +" en el sistema");

        return categoryDB;
    }

    @Override
    public ApiResponseDto executeCreateMassiveCategories(MultipartFile file) throws BadRequestException {
        try {
            List<CategoryDto> categoryListExcel = ExcelHelper.readDataCategoryExcel(file.getInputStream());
            List<CategoryEntity> categoryListSave =categoryListExcel.stream().map(category->{
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setName(category.getName());
                categoryEntity.setDescription(category.getDescription());
                categoryEntity.setStatus(Status.ACTIVE);
                return categoryEntity;
            }).toList();

            this.categoryRepository.saveAll(categoryListSave);

            return new ApiResponseDto(HttpStatus.CREATED.value(),"Se inserto todas la categorias de forma exitosa", "total de registros: "+categoryListSave.size());
        }catch (Exception e){
            throw new BadRequestException("Error al procesar el archivo excel ", e);
        }
    }


}
