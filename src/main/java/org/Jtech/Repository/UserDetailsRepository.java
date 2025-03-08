package org.Jtech.Repository;

import org.Jtech.Entity.UserDetails;
import org.Jtech.DTO.UserDetailsDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserDetailsRepository extends CrudRepository<UserDetails, Long> {


    @Query("SELECT new org.Jtech.DTO.UserDetailsDTO(u.user.userId, u.skinType,u.hairType, u.age, u.gender, u.skinColour, u.anyAllergies, u.bmi, u.weight) " +
            "FROM UserDetails u WHERE u.user.userId = :id")
    UserDetailsDTO userdetaildata(@Param("id") Long id);
}