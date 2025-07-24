package com.asmdev.api.pos.persistence.repository;

import com.asmdev.api.pos.persistence.entity.CashMovementsEntity;
import com.asmdev.api.pos.utils.status.CashMovementsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CashMovementsRepository extends JpaRepository<CashMovementsEntity,String>, JpaSpecificationExecutor<CashMovementsEntity> {

    @Query("SELECT COUNT(cm) FROM CashMovementsEntity cm WHERE cm.cashRegister.id = :cashRegisterId AND cm.status = :status")
    long countByCashRegisterIdAndStatus(
            @Param("cashRegisterId") String cashRegisterId,
            @Param("status") CashMovementsStatus status
    );


}
