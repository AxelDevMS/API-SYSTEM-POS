package com.asmdev.api.pos.persistence.specification;

import com.asmdev.api.pos.persistence.entity.InventoryMovementsEntity;
import com.asmdev.api.pos.utils.method.InventoryMovementType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpecificationInventoryMovements {
    public static Specification<InventoryMovementsEntity> withFilter(String userId, String productId, String type, String startDate, String endDate){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");


            if (userId != null && !userId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));

            if (productId != null && !productId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("product").get("id"), productId));

            if (type != null && !type.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("type"), InventoryMovementType.valueOf(type)));

            try {
                if (startDate != null && !startDate.isEmpty()) {
                    Date start = formatDate.parse(startDate);
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), start));
                }

                if (endDate != null && !endDate.isEmpty()) {
                    Date end = formatDate.parse(endDate); // dd/MM/yyyy
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), end));
                }
            } catch (Exception e) {
                throw new RuntimeException("Formato de fecha inválido. Usa dd/MM/yyyy"+ e.getMessage());
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
