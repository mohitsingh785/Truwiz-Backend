package org.Jtech.Repository;

import org.Jtech.DTO.UserDetailsView;
import org.Jtech.Entity.User;
import org.Jtech.Entity.UserDetails;
import org.Jtech.DTO.UserDetailsDTO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    Optional<UserDetailsView> findByUserUserId(Long userId);
    // Profile with allergies (+ allergy names) and concerns in one query, no N+1.
    // Both collections are Sets, so fetching them together is allowed.
    @EntityGraph(attributePaths = {"userAllergies", "userAllergies.allergy", "userConcerns"})
    Optional<UserDetails> findWithProfileByUserUserId(Long userId);
}