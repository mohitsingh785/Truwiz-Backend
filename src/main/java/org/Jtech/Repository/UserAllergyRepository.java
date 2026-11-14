package org.Jtech.Repository;

import org.Jtech.Entity.UserAllergy;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAllergyRepository extends JpaRepository<UserAllergy,Long> {

    // Loads the allergy in the same query: login reads every allergy name,
    // so LAZY alone would fire one extra query per allergy (N+1).
    @EntityGraph(attributePaths = "allergy")
    List<UserAllergy> findByUserDetailsDetailsId(Long detailsId);
}
