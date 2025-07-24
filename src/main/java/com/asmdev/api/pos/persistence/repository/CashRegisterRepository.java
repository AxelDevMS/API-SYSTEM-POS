package com.asmdev.api.pos.persistence.repository;

import com.asmdev.api.pos.persistence.entity.CashRegisterEntity;
import com.asmdev.api.pos.utils.status.CashRegisterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CashRegisterRepository extends JpaRepository<CashRegisterEntity,String>, JpaSpecificationExecutor<CashRegisterEntity> {

    // Buscar si el usuario tiene una caja abierta
    boolean existsByUserIdAndStatus(String userId, CashRegisterStatus status);
}
