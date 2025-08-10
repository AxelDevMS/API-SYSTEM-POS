package com.asmdev.api.pos.persistence.specification;

import com.asmdev.api.pos.persistence.entity.UserEntity;
import com.asmdev.api.pos.utils.status.UserStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpecificationUser {

    public static Specification<UserEntity> withFilter(String userId, String status, String roleId,  String hireDate){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null && !userId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("id"), userId));

            if (status != null && !status.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("status"), UserStatus.valueOf(status)));

            if (roleId != null && !roleId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("role").get("id"), roleId));

            if (hireDate != null && !hireDate.isEmpty()) {
                try {
                    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
                    Date parsedDate = formatDate.parse(hireDate);

                    // Calcular inicio y fin del día
                    Date startOfDay = parsedDate;
                    Date endOfDay = new Date(parsedDate.getTime() + (24 * 60 * 60 * 1000) - 1);

                    predicates.add(criteriaBuilder.between(root.get("hireDate"), startOfDay, endOfDay));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Formato de fecha inválido. Se espera dd/MM/yyyy.");
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
