package com.asmdev.api.pos.persistence.repository;

import com.asmdev.api.pos.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<CategoryEntity,String>, JpaSpecificationExecutor<CategoryEntity> {
}
