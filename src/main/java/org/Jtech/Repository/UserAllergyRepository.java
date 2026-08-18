package org.Jtech.Repository;

import org.Jtech.Entity.UserAllergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAllergyRepository extends JpaRepository<UserAllergy,Long> {
    List<UserAllergy> findByUserDetailsDetailsId(Long detailsId);
}
