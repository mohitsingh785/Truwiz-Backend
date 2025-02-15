package org.Jtech.Repository;

import org.Jtech.Entity.OTP;
import org.Jtech.Model.OtpResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OtpRepository  extends CrudRepository<OTP,Integer> {


    @Query("SELECT new org.Jtech.Model.OtpResponse(o.otp, o.createdAt) FROM OTP o WHERE o.userId = :userId")
    Optional<OtpResponse> findOtpAndCreatedAtByUserId(@Param("userId") Integer userId);


}
