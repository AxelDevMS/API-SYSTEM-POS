package com.asmdev.api.pos.persistence.specification;

import com.asmdev.api.pos.persistence.entity.PermissionEntity;
import com.asmdev.api.pos.utils.status.ModuleSystem;
import com.asmdev.api.pos.utils.status.Status;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationPermission {

    public static Specification<PermissionEntity> withFilter(String permissionId, String module, String status){
        return (root, query, cb)->{
            List<Predicate> predicates = new ArrayList<>();

            if (permissionId != null && !permissionId.isEmpty())
                predicates.add(cb.equal(root.get("id"), permissionId));

            if (module != null  && !module.isEmpty())
                predicates.add(cb.equal(root.get("module"), ModuleSystem.valueOf(module)));

            if (status != null && !status.isEmpty())
                predicates.add(cb.equal(root.get("status"), Status.valueOf(status)));


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
