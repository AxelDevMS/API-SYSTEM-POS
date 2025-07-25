package com.asmdev.api.pos.persistence.repository;

import com.asmdev.api.pos.persistence.entity.InventoryMovementsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovementsEntity,String>, JpaSpecificationExecutor<InventoryMovementsEntity> {
}
