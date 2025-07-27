package com.asmdev.api.pos.persistence.repository;

import com.asmdev.api.pos.persistence.entity.SaleItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SaleItemRepository extends JpaRepository<SaleItemEntity,String>, JpaSpecificationExecutor<SaleItemEntity> {
}
