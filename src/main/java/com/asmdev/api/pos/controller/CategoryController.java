package com.asmdev.api.pos.controller;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.CategoryDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.security.preauthorize.CategoryPreAuthorize.*;
import com.asmdev.api.pos.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/pos/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @CanListCategory
    @GetMapping("/get-alls")
    public ResponseEntity<ApiResponseDto> executeGetListCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String status
    ) throws NotFoundException {
        ApiResponseDto response = this.categoryService.executeGetListCategories(page, size, categoryId, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CanListCategory
    @GetMapping("/list-select")
    public ResponseEntity<ApiResponseDto> executeGetListCategoriesBySelect() throws NotFoundException {
        ApiResponseDto response = this.categoryService.executeGetListCategoriesBySelect();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CanGetCategory
    @GetMapping("/get/{categoryId}")
    public ResponseEntity<ApiResponseDto> executeGetCategory(@PathVariable String categoryId) throws NotFoundException {
        ApiResponseDto response = this.categoryService.executeGetCategory(categoryId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CanCreateCategory
    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            BindingResult bindingResult
    ) throws BadRequestException {
        ApiResponseDto response = this.categoryService.executeCreateCategory(categoryDto, bindingResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @CanUpdateCategory
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponseDto> executeUpdateCategory(
            @PathVariable String categoryId,
            @Valid @RequestBody CategoryDto categoryDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.categoryService.executeUpdateCategory(categoryId, categoryDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CanDeletedCategory
    @PatchMapping("/disabled/{categoryId}")
    public ResponseEntity<ApiResponseDto> executeDisabledCategory(
            @PathVariable String categoryId,
            @Valid @RequestBody DisabledRegisterDto disabledRegisterDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.categoryService.executeDisabledCategory(categoryId, disabledRegisterDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CanImportCategory
    @PostMapping("upload")
    public ResponseEntity<ApiResponseDto> executeUploadMassiveCategories(@RequestParam("file") MultipartFile file) throws BadRequestException {
        ApiResponseDto response = this.categoryService.executeCreateMassiveCategories(file);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
}
