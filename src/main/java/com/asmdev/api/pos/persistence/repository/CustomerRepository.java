package com.asmdev.api.pos.persistence.repository;

import com.asmdev.api.pos.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRepository  extends JpaRepository<CustomerEntity, String>, JpaSpecificationExecutor<CustomerEntity> {

}
