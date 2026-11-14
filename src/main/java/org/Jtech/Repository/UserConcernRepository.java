package org.Jtech.Repository;

import org.Jtech.Entity.UserConcern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserConcernRepository extends JpaRepository<UserConcern, Long> {

    List<UserConcern> findByUserDetailsDetailsId(Long detailsId);
}
