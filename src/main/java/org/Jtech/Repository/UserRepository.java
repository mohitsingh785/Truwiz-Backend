package org.Jtech.Repository;

import jakarta.transaction.Transactional;
import org.Jtech.Entity.User;
import org.Jtech.DTO.UserData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {


    @Query("SELECT new org.Jtech.DTO.UserData(u.userId, u.userName, u.email, u.phoneNumber,u.password) FROM User  u WHERE u.userId = :id")
    UserData alldata(@Param("id") Long id);





    @Query("SELECT new org.Jtech.DTO.UserData(u.userId,u.userName, u.email, u.phoneNumber, u.password) FROM User u WHERE u.email = :email")
    Optional<UserData> findByEmailAndPassword(@Param("email") String email);


    @Query("SELECT u.userId FROM User u WHERE u.email = :email")
    Long findUserIdByEmail(@Param("email") String email);


    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.userId = :userId")
    int updatePassword(@Param("userId") Long userId, @Param("password") String newPassword);




}