package com.asmdev.api.pos.persistence.repository;

import com.asmdev.api.pos.persistence.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<SupplierEntity,String>, JpaSpecificationExecutor<SupplierEntity> {
    Optional<SupplierEntity> findByEmail(String email);
}
