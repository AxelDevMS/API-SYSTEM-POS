package com.asmdev.api.pos.persistence.repository;

import com.asmdev.api.pos.persistence.entity.PurchaseItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ItemPurchaseRepository extends JpaRepository<PurchaseItemsEntity,String>, JpaSpecificationExecutor<PurchaseItemsEntity> {
}
