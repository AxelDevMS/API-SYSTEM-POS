package com.asmdev.api.pos.persistence.specification;

import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.persistence.entity.CashRegisterEntity;
import com.asmdev.api.pos.utils.status.CashRegisterStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpecificationCashRegister {

    public static Specification<CashRegisterEntity> withFilter(String cashRegisterId, String status, String startDate, String endDate){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

            if (cashRegisterId != null && !cashRegisterId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("id"), cashRegisterId));

            if (status != null && !status.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("status"), CashRegisterStatus.valueOf(status)));

            try {
                if (startDate != null && !startDate.isEmpty()) {
                    Date start = formatDate.parse(startDate);
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("opened").get("At"), start));
                }

                if (endDate != null && !endDate.isEmpty()) {
                    Date end = formatDate.parse(endDate); // dd/MM/yyyy
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("closed").get("At"), end));
                }
            } catch (Exception e) {
                throw new RuntimeException("Formato de fecha inválido. Usa dd/MM/yyyy", e);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
