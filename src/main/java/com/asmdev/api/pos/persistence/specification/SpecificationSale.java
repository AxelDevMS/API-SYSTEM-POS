package com.asmdev.api.pos.persistence.specification;

import com.asmdev.api.pos.persistence.entity.SaleEntity;
import com.asmdev.api.pos.utils.status.PurchaseStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpecificationSale {

    public static Specification<SaleEntity> withFilter(String customerId, String saleId, String userId, String status, String startDate, String endDate){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

            if (customerId != null && !customerId.isEmpty())
                predicateList.add(criteriaBuilder.equal(root.get("cutomer").get("id"), customerId));

            if (saleId != null && !saleId.isEmpty())
                predicateList.add(criteriaBuilder.equal(root.get("id"), saleId));

            if (userId != null  && !userId.isEmpty())
                predicateList.add(criteriaBuilder.equal(root.get("user").get("id"), userId));

            if (status != null && !status.isEmpty())
                predicateList.add(criteriaBuilder.equal(root.get("status"), PurchaseStatus.valueOf(status)));

            try {
                if (startDate != null && !startDate.isEmpty()){
                    Date start = formatDate.parse(startDate);
                    predicateList.add(criteriaBuilder.equal(root.get("createdAt"), start));
                }

                if (endDate != null && !endDate.isEmpty()){
                    Date end = formatDate.parse(endDate);
                    predicateList.add(criteriaBuilder.equal(root.get("createdAt"), end));
                }
            }catch (Exception e){
                throw new RuntimeException("Formato de fecha invalido "+ e.getMessage());
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        });
    }
}
