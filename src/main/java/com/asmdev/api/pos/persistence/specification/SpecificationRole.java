package com.asmdev.api.pos.persistence.specification;

import com.asmdev.api.pos.persistence.entity.RoleEntity;
import com.asmdev.api.pos.utils.status.Status;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationRole {

    public static Specification<RoleEntity> withFilter(String roleId, String status){
      return (root, query, cb)->{
          List<Predicate> predicates = new ArrayList<>();

          if (roleId != null && !roleId.isEmpty())
              predicates.add(cb.equal(root.get("id"), roleId));

          if (status != null && !status.isEmpty())
              predicates.add(cb.equal(root.get("status"), Status.valueOf(status)));

          return cb.and(predicates.toArray(new Predicate[0]));
      };
    }
}
