package com.asmdev.api.pos.controller;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.dto.SupplierDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.security.preauthorize.SupplierPreAuthorize.*;
import com.asmdev.api.pos.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/pos/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @CanCreatedSupplier
    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreateSupplier(
            @Valid @RequestBody SupplierDto supplierDto,
            BindingResult bindingResult
    ) throws BadRequestException {
        ApiResponseDto response = this.supplierService.executeCreateSupplier(supplierDto, bindingResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @CanUpdatedSupplier
    @PutMapping("/update/{supplierId}")
    public ResponseEntity<ApiResponseDto> executeUpdateSupplier(
            @PathVariable String supplierId,
            @Valid @RequestBody SupplierDto supplierDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.supplierService.executeUpdateSupplier(supplierId, supplierDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CanDeletedSupplier
    @PatchMapping("/disabled/{supplierId}")
    public ResponseEntity<ApiResponseDto> executeDisabledSupplier(
            @PathVariable String supplierId,
            @Valid @RequestBody DisabledRegisterDto disabledRegisterDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.supplierService.executeDisabledSupplier(supplierId, disabledRegisterDto, bindingResult);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CanListSupplier
    @GetMapping("/get-alls")
    public ResponseEntity<ApiResponseDto> executeGetSupplierList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String supplierId,
            @RequestParam(required = false) String status
    ) throws NotFoundException {
        ApiResponseDto response = this.supplierService.executeGetSupplierList(page, size, supplierId, status);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CanListSupplier
    @GetMapping("/select")
    public ResponseEntity<ApiResponseDto> executeGetSupplierListBySelect() throws NotFoundException {
        ApiResponseDto response = this.supplierService.executeGetSupplierListBySelect();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CanGetSupplier
    @GetMapping("/get/{supplierId}")
    public ResponseEntity<ApiResponseDto> executeGetSupplier(@PathVariable String supplierId) throws NotFoundException {
        ApiResponseDto response = this.supplierService.executeGetSupplier(supplierId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CanImportSupplier
    @PostMapping("/import")
    public ResponseEntity<ApiResponseDto> executeImportSupplier(@RequestParam("file")MultipartFile file) throws BadRequestException {
        ApiResponseDto response = this.supplierService.executeImportSupplier(file);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

}
