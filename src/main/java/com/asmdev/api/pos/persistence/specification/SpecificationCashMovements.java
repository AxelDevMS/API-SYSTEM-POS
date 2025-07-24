package com.asmdev.api.pos.persistence.specification;

import com.asmdev.api.pos.persistence.entity.CashMovementsEntity;
import com.asmdev.api.pos.utils.status.CashMovementsStatus;
import com.asmdev.api.pos.utils.status.TypeCashMovement;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpecificationCashMovements {

    public static Specification<CashMovementsEntity> withFilter( String userId, String cashRegisterId, String type, String status, String startDate, String endDate){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");


            if (userId != null && !userId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));

            if (cashRegisterId != null && !cashRegisterId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("cash").get("register").get("id"), cashRegisterId));

            if (type != null && !type.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("type"), TypeCashMovement.valueOf(type)));

            if (status != null && !status.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("status"), CashMovementsStatus.valueOf(status)));

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
