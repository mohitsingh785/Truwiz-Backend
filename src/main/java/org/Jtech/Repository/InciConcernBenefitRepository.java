package org.Jtech.Repository;

import org.Jtech.Constant.Concern;
import org.Jtech.Entity.InciConcernBenefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface InciConcernBenefitRepository extends JpaRepository<InciConcernBenefit, Long> {

    // One query for all ingredients of a product. Never call per ingredient (N+1).
    List<InciConcernBenefit> findByInciIngredientInciIdInAndConcernIn(
            Collection<Long> inciIds, Collection<Concern> concerns);
}