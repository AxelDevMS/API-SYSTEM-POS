package com.asmdev.api.pos.persistence.specification;

import com.asmdev.api.pos.persistence.entity.ProductEntity;
import com.asmdev.api.pos.utils.status.ProductStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationProduct {

    public static Specification<ProductEntity> withFilter(String productId, String categoryId, String status){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productId != null && !productId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("id"), productId));

            if (categoryId != null && !categoryId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));

            if (status != null && !status.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("status"), ProductStatus.valueOf(status)));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
