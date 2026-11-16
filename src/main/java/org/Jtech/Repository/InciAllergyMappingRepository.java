package org.Jtech.Repository;

import org.Jtech.DTO.InciAllergyLink;
import org.Jtech.Entity.InciAllergyMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface InciAllergyMappingRepository extends JpaRepository<InciAllergyMapping, Long> {

    @Query("""
           select new org.Jtech.DTO.InciAllergyLink(m.inciIngredient.inciId, m.allergy.allergyId)
           from InciAllergyMapping m
           where m.inciIngredient.inciId in :ids
           """)
    List<InciAllergyLink> findLinksByInciIds(@Param("ids") Set<Long> ids);
}
