package com.asmdev.api.pos.persistence.repository;

import com.asmdev.api.pos.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity,String>, JpaSpecificationExecutor<ProductEntity> {
    Optional<ProductEntity> findByCode(String code);

    @Query("SELECT p FROM ProductEntity p WHERE p.stock <= p.minimumStock")
    List<ProductEntity> findByStockLessThanEqualMinimumStock();
}
