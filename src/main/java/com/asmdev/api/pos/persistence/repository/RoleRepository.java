package com.asmdev.api.pos.persistence.repository;

import com.asmdev.api.pos.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<RoleEntity,String>, JpaSpecificationExecutor<RoleEntity> {
}
