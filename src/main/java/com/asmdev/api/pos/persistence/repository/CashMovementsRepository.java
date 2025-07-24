package com.asmdev.api.pos.persistence.repository;

import com.asmdev.api.pos.persistence.entity.CashMovementsEntity;
import com.asmdev.api.pos.utils.status.CashMovementsStatus;
import com.asmdev.api.pos.utils.status.TypeCashMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CashMovementsRepository extends JpaRepository<CashMovementsEntity,String>, JpaSpecificationExecutor<CashMovementsEntity> {

    @Query("SELECT COUNT(cm) FROM CashMovementsEntity cm WHERE cm.cashRegister.id = :cashRegisterId AND cm.status = :status")
    long countByCashRegisterIdAndStatus(
            @Param("cashRegisterId") String cashRegisterId,
            @Param("status") CashMovementsStatus status
    );


    @Query("SELECT SUM(cm.amount) FROM CashMovementsEntity cm WHERE cm.cashRegister.id = :cashRegisterId AND cm.type = :type AND cm.status = :status")
    BigDecimal sumByCashRegisterAndTypeAndStatus(
            @Param("cashRegisterId") String cashRegisterId,
            @Param("type") TypeCashMovement type,
            @Param("status") CashMovementsStatus status
    );

    List<CashMovementsEntity> findAllByCashRegisterIdAndStatus(String id, CashMovementsStatus status);




}
