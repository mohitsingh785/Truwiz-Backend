package org.Jtech.specification;

import org.Jtech.Constant.BrandStatus;
import org.Jtech.Entity.Brand;
import org.springframework.data.jpa.domain.Specification;

public class BrandSpecifications {

    public static Specification<Brand> hasStatus(BrandStatus status) {
        return (root, query, cb) -> cb.equal(root.get("brandStatus"), status);
    }
}
