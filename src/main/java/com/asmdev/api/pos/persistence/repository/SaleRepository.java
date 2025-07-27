package com.asmdev.api.pos.persistence.repository;

import com.asmdev.api.pos.persistence.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SaleRepository extends JpaRepository<SaleEntity, String>, JpaSpecificationExecutor<SaleEntity> {
}
