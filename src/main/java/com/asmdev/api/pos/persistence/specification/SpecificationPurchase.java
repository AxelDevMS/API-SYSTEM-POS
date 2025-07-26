package com.asmdev.api.pos.persistence.specification;

import com.asmdev.api.pos.persistence.entity.PurchaseEntity;
import com.asmdev.api.pos.utils.status.PurchaseStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpecificationPurchase {

    public static Specification<PurchaseEntity> withFilter(String supplierId, String purchaseId, String userId, String status, String startDate, String endDate){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

            if (supplierId != null && !supplierId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("supplier").get("id"), supplierId));

            if (purchaseId != null && !purchaseId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("id"), purchaseId));

            if (userId != null && !userId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));

            if (status != null && !status.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("status"), PurchaseStatus.valueOf(status)));

            try {
                if (startDate != null && !startDate.isEmpty()){
                    Date start = formatDate.parse(startDate);
                    predicates.add(criteriaBuilder.equal(root.get("createdAt"), start));
                }

                if (endDate != null && !endDate.isEmpty()){
                    Date end = formatDate.parse(endDate);
                    predicates.add(criteriaBuilder.equal(root.get("createdAt"), end));
                }
            }catch (Exception e){
                throw new RuntimeException("Formato de fecha invalido "+ e.getMessage());
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
