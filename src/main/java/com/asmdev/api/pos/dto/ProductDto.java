package com.asmdev.api.pos.dto;

import com.asmdev.api.pos.utils.status.ProductStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;

public class ProductDto {

    private String id;

    private String url;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String name;


    @NotBlank(message = "El código del producto es obligatorio")
    @Size(max = 50, message = "El código no debe superar los 50 caracteres")
    private String code;

    @NotBlank(message = "La descripción del producto es obligatoria")
    @Size(max = 255, message = "La descripción no debe superar los 255 caracteres")
    private String description;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private int minimumStock;

    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de compra debe ser mayor que 0")
    private BigDecimal purchasePrice;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor que 0")
    private BigDecimal salePrice;

    private ProductStatus status;

    private CategoryDto category;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
