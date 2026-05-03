package org.Jtech.Repository;

import org.Jtech.Constant.OtpPurpose;
import org.Jtech.Entity.OTP;
import org.Jtech.Model.OtpResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OtpRepository  extends CrudRepository<OTP,Integer> {


    Optional<OTP> findTopByEmailAndOtpPurposeOrderByUpdatedAtDesc(
            String email,
            OtpPurpose otpPurpose
    );


    Optional<OTP> findTopByEmailOrderByUpdatedAtDesc(String email);



}
