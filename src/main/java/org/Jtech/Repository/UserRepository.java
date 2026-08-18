package org.Jtech.Repository;

import jakarta.transaction.Transactional;
import org.Jtech.Entity.User;
import org.Jtech.DTO.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {




    User findByEmailAndPassword(String email,String password);

    Optional<User> findByEmail(String email);


    Long findUserIdByEmail(String email);


    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.userId = :userId")
    int updatePassword(@Param("userId") Long userId, @Param("password") String newPassword);


    boolean existsByEmail(String email);




}