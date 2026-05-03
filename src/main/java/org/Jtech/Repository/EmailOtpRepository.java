package org.Jtech.Repository;

import org.Jtech.Entity.EmailVerify;
import org.Jtech.Model.OtpResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailOtpRepository extends CrudRepository<EmailVerify,Integer> {


//    @Query("SELECT new org.Jtech.Model.OtpResponse(o.otp, o.createdAt) FROM EmailVerify o WHERE o.id = :otpId")
//    Optional<OtpResponse> findOtpAndCreatedAtByUserId(@Param("otpId") Integer otpId);
}
