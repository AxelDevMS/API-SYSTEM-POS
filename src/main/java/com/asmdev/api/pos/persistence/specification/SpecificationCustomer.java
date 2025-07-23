package com.asmdev.api.pos.persistence.specification;

import com.asmdev.api.pos.persistence.entity.CustomerEntity;
import com.asmdev.api.pos.utils.status.Status;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationCustomer {

    public static Specification<CustomerEntity> withFilter(String customerId, String status){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (customerId != null && !customerId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("id"), customerId));

            if (status != null && !status.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("status"), Status.valueOf(status)));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
