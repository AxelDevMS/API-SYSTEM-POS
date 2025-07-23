package com.asmdev.api.pos.controller;


import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.dto.ProductDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/pos/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/import")
    public ResponseEntity<ApiResponseDto> executeImportProducts(@RequestParam("file")MultipartFile file) throws BadRequestException {
        ApiResponseDto response = this.productService.executeImportMassiveProducts(file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreateProduct(
            @Valid @RequestBody ProductDto productDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response= this.productService.executeCreateProduct(productDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponseDto> executeUpdateProduct(
            @PathVariable String productId,
            @Valid @RequestBody ProductDto productDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.productService.executeUpdateProduct(productId, productDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/select")
    public ResponseEntity<ApiResponseDto> executeGetProductListBySelect() throws NotFoundException {
        ApiResponseDto response = this.productService.executeGetProductListBySelect();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("get/{productId}")
    public ResponseEntity<ApiResponseDto> executeGetProduct(@PathVariable String productId) throws NotFoundException {
        ApiResponseDto response = this.productService.executeGetProduct(productId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("get/low-stock")
    public ResponseEntity<ApiResponseDto> executeGetLowStockProductList() throws NotFoundException {
        ApiResponseDto response = this.productService.executeGetLowStockProductList();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportProducts() throws BadRequestException {
        byte[] fileBytes = this.productService.executeExportProducts();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("productos.xlsx")
                .build());

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

    @PatchMapping("/disabled/{productId}")
    public ResponseEntity<ApiResponseDto> executeDisabledProduct(
            @PathVariable String productId,
            @Valid @RequestBody DisabledRegisterDto disabledRegisterDto,
            BindingResult bindingResult
    ) throws NotFoundException, BadRequestException {
        ApiResponseDto response = this.productService.executeDisabledProduct(productId, disabledRegisterDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/get-alls")
    public ResponseEntity<ApiResponseDto> executeGetProductList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String productId,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String status
    ) throws NotFoundException {
        ApiResponseDto response = this.productService.executeGetProductList(page, size, productId, categoryId, status);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }




}
