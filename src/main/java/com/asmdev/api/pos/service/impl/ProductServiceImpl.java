package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.*;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.CategoryMapper;
import com.asmdev.api.pos.mapper.ProductMapper;
import com.asmdev.api.pos.persistence.entity.CategoryEntity;
import com.asmdev.api.pos.persistence.entity.ProductEntity;
import com.asmdev.api.pos.persistence.repository.ProductRepository;
import com.asmdev.api.pos.persistence.specification.SpecificationProduct;
import com.asmdev.api.pos.service.CategoryService;
import com.asmdev.api.pos.service.ProductService;
import com.asmdev.api.pos.utils.helper.ExcelHelper;
import com.asmdev.api.pos.utils.status.ProductStatus;
import com.asmdev.api.pos.utils.status.Status;
import com.asmdev.api.pos.utils.validations.ValidateInputs;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ValidateInputs validateInputs;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ApiResponseDto executeImportMassiveProducts(MultipartFile file) throws BadRequestException {
        try {
            List<ProductDto> productListExcel = ExcelHelper.readDataProductsExcel(file.getInputStream());
            List<ProductEntity> productListSave = productListExcel.stream().map(productDto -> {
                ProductEntity productEntity = new ProductEntity();
                productEntity.setName(productDto.getName());
                productEntity.setUrl(productDto.getUrl());
                productEntity.setCode(productDto.getCode());
                productEntity.setDescription(productDto.getDescription());
                productEntity.setStock(productDto.getStock());
                productEntity.setMinimumStock(productDto.getMinimumStock());
                productEntity.setPurchasePrice(productDto.getPurchasePrice());
                productEntity.setSalePrice(productDto.getSalePrice());
                productEntity.setStatus(ProductStatus.ACTIVE);
                productEntity.setCategory(categoryMapper.convertToEntity(productDto.getCategory()));
                return productEntity;
            }).toList();
            this.productRepository.saveAll(productListSave);
            return new ApiResponseDto(HttpStatus.CREATED.value(),"Se inserto todas la categorias de forma exitosa", "total de registros: "+productListSave.size());
        }catch (Exception e){
            throw new BadRequestException("Error al procesar el archivo excel "+ e.getMessage());
        }
    }

    @Override
    public ApiResponseDto executeCreateProduct(ProductDto productDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        ProductEntity productBD = this.productRepository.findByCode(productDto.getCode()).orElse(null);
        if (productBD != null)
            throw new BadRequestException("Ya existe un producto con este mismo codigo: "+productDto.getCode());

        CategoryEntity categoryBD = this.categoryService.getCategoryById(productDto.getCategory().getId());
        if (!categoryBD.getStatus().equals(Status.ACTIVE))
            throw new BadRequestException("No puedes asignar esta categoria ya que tiene un status como: "+categoryBD.getStatus());

        productBD = this.productMapper.convertToEntity(productDto);
        productBD.setCategory(categoryBD);
        productBD = this.productRepository.save(productBD);

        return new ApiResponseDto(HttpStatus.CREATED.value(),"El producto se ha registrado exitosamente", this.productMapper.convertToDto(productBD));
    }

    @Override
    public ApiResponseDto executeUpdateProduct(String productId, ProductDto productDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        ProductEntity productBD = this.getProductById(productId);

        CategoryEntity categoryBD = this.categoryService.getCategoryById(productDto.getCategory().getId());
        if (!categoryBD.getStatus().equals(Status.ACTIVE))
            throw new BadRequestException("No puedes asignar esta categoria ya que tiene un status como: "+categoryBD.getStatus());

        productBD.setName(productDto.getName());
        productBD.setUrl(productDto.getUrl());
        productBD.setCode(productDto.getCode());
        productBD.setDescription(productDto.getDescription());
        productBD.setStock(productDto.getStock());
        productBD.setMinimumStock(productDto.getMinimumStock());
        productBD.setPurchasePrice(productDto.getPurchasePrice());
        productBD.setSalePrice(productDto.getSalePrice());
        productBD.setStatus(productDto.getStatus());
        productBD.setCategory(categoryBD);

        productBD = this.productRepository.save(productBD);
        return new ApiResponseDto(HttpStatus.OK.value(),"Se actualizo el producto de forma exitosa", this.productMapper.convertToDto(productBD));
    }

    @Override
    public ApiResponseDto executeGetProductList(int page, int size, String productId, String categoryId, String status) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Specification<ProductEntity> filter = SpecificationProduct.withFilter(productId, categoryId, status);
        Page<ProductEntity> productListBD = this.productRepository.findAll(filter,pageable);

        if (productListBD.isEmpty())
            throw new NotFoundException("No hay registros en el sistema");

        List<ProductDto> productList = productListBD.getContent().stream().map(productMapper::convertToDto).toList();
        ListDataPaginationDto pagination = new ListDataPaginationDto();
        pagination.setData(Collections.singletonList(productList));
        pagination.setTotalElements((int) productListBD.getTotalElements());

        return new ApiResponseDto(HttpStatus.OK.value(),"Listado de productos",pagination);
    }

    @Override
    public ApiResponseDto executeGetProductListBySelect() throws NotFoundException {
        List<ProductEntity> productListBD = this.productRepository.findAll();
        if (productListBD.isEmpty())
            throw new NotFoundException("No tienes productos registrados en el sistema");

        List<ProductDto> productList = productListBD.stream().map(productMapper::convertToDto).toList();
        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de productos", productList);
    }

    @Override
    public ApiResponseDto executeGetProduct(String productId) throws NotFoundException {
        ProductEntity product = this.getProductById(productId);
        return new ApiResponseDto(HttpStatus.OK.value(), "Información del producto", this.productMapper.convertToDto(product));
    }

    @Override
    public ApiResponseDto executeGetLowStockProductList() throws NotFoundException {
        List<ProductEntity> lowStockProducts = productRepository.findByStockLessThanEqualMinimumStock();
        if (lowStockProducts.isEmpty())
            throw new NotFoundException("No existen productos que tenga stock por debajo del stock minimo");

        List<ProductDto> productList = lowStockProducts.stream().map(productMapper::convertToDto).toList();
        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de productos", productList);
    }

    @Override
    public byte[] executeExportProducts() throws BadRequestException {
        List<ProductEntity> products = productRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Productos");

            createHeaderRow(workbook, sheet);
            createDataRows(workbook, sheet, products);

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new BadRequestException("Error al exportar información de productos "+ e.getMessage());
        }
    }

    private void createHeaderRow(Workbook workbook, Sheet sheet) {
        String[] headers = {"Nombre", "Código", "Categoría", "Stock", "Stock Mínimo", "Precio Compra", "Precio Venta", "Estatus", "Creado"};

        Row headerRow = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
    }

    private void createDataRows(Workbook workbook, Sheet sheet, List<ProductEntity> products) {
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm"));

        int rowNum = 1;
        for (ProductEntity product : products) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(product.getName());
            row.createCell(1).setCellValue(product.getCode());
            row.createCell(2).setCellValue(product.getCategory() != null ? product.getCategory().getName() : "Sin categoría");
            row.createCell(3).setCellValue(product.getStock());
            row.createCell(4).setCellValue(product.getMinimumStock());
            row.createCell(5).setCellValue(product.getPurchasePrice().doubleValue());
            row.createCell(6).setCellValue(product.getSalePrice().doubleValue());
            row.createCell(7).setCellValue(product.getStatus().name());

            Cell dateCell = row.createCell(8);
            dateCell.setCellValue(product.getCreatedAt());
            dateCell.setCellStyle(dateStyle);
        }

        for (int i = 0; i < 9; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    @Override
    public ApiResponseDto executeDisabledProduct(String productId, DisabledRegisterDto disabledRegisterDto, BindingResult bindingResult) throws NotFoundException, BadRequestException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        ProductEntity productBD = this.getProductById(productId);
        if (ProductStatus.valueOf(disabledRegisterDto.getStatus()).equals(productBD.getStatus()))
            throw new BadRequestException("El producto ya tiene asignado el estado '" + disabledRegisterDto.getStatus() + "'");

        CategoryEntity categoryBD = this.categoryService.getCategoryById(productBD.getCategory().getId());

        productBD.setStatus(ProductStatus.valueOf(disabledRegisterDto.getStatus()));
        productBD.setCategory(categoryBD);
        productBD = this.productRepository.save(productBD);
        return new ApiResponseDto(HttpStatus.OK.value(), "El producto cambio de status existosamente", this.productMapper.convertToDto(productBD));
    }

    @Override
    public ProductEntity getProductById(String productId) throws NotFoundException {
        ProductEntity product = this.productRepository.findById(productId).orElse(null);
        if (product == null)
            throw new NotFoundException("No existe este producto en el sistema");

        return product;
    }

    @Override
    public ProductEntity updateStock(String productId, int stock) throws NotFoundException {
        ProductEntity product = this.getProductById(productId);
        product.setStock(stock);
        product = this.productRepository.save(product);
        return product;
    }
}
