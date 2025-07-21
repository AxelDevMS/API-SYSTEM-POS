package com.asmdev.api.pos.persistence.specification;

import com.asmdev.api.pos.persistence.entity.CategoryEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationCategory {

    public static Specification<CategoryEntity> withFilter(String categoryId, String status){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if (categoryId != null && !categoryId.isEmpty())
                predicateList.add(criteriaBuilder.equal(root.get("id"), categoryId));

            if (status != null && !status.isEmpty())
                predicateList.add(criteriaBuilder.equal(root.get("status"), status));

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
