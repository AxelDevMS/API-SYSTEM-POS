package com.asmdev.api.pos.persistence.repository;

import com.asmdev.api.pos.persistence.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity,String>, JpaSpecificationExecutor<PurchaseEntity> {
}
