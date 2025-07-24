package com.asmdev.api.pos.persistence.specification;

import com.asmdev.api.pos.persistence.entity.CashRegisterEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationCashRegister {

    public static Specification<CashRegisterEntity> withFilter(String cashRegisterId, String status, String startDate, String endDate){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
