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

    public static Specification<UserEntity> withFilter(String userId, String status, String roleId, String neighborhood, String municipality, String state, String hireDate){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null && !userId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("id"), userId));

            if (status != null && !status.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("status"), UserStatus.valueOf(status)));

            if (roleId != null && !roleId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("role").get("id"), roleId));

            if (neighborhood != null && !neighborhood.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("neighborhood"), neighborhood));

            if (municipality != null && !municipality.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("municipality"), municipality));

            if (state != null && !state.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("state"), state));

            if (hireDate != null && !hireDate.isEmpty()){
                try {
                    // Suponiendo formato ISO: yyyy-MM-dd
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date parsedDate = sdf.parse(hireDate);
                    predicates.add(criteriaBuilder.equal(root.get("hireDate"), parsedDate));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Formato de fecha inválido. Se espera yyyy-MM-dd.");
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
