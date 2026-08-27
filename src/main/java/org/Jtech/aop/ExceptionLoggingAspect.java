package org.Jtech.aop;


import org.Jtech.Exception.*;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Logging Aspect
 * <p>
 * Purpose:
 * Provides cross-cutting logging functionality for service-layer
 * methods to track method invocation flow.
 * <p>
 * Scope:
 * - Logs execution of selected service methods
 * <p>
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 * <p>
 * Notes:
 * This aspect is intended for debugging and monitoring purposes.
 * It should not contain business logic or modify application behavior.
 */


@Aspect
@Component
public class ExceptionLoggingAspect {

    private static final Logger logger =
            LoggerFactory.getLogger(ExceptionLoggingAspect.class);


    @AfterThrowing(
            pointcut = "execution(* org.Jtech.Service..*(..))",
            throwing = "ex"
    )
    public void logException(Throwable ex) {

        if (ex instanceof InvalidCredentialsException invalidCredentialsException) {
            logger.warn("Credential validation failed for user={}", invalidCredentialsException.getEmail());
        } else if (ex instanceof UserNotFoundException userNotFoundException) {
            logger.warn("User not found. userId={}", userNotFoundException.getUserId());
        } else if (ex instanceof RequestFailedException requestFailedException) {
            logger.warn(
                    "Password reset failed. user={}, reason={}",
                    requestFailedException.getEmail(),
                    requestFailedException.getMessage()
            );
        }
        else if(ex instanceof UserAlreadyExistsException userAlreadyExistsException){
            logger.warn("User Already exist for user={}", userAlreadyExistsException.getEmail());
        }
        else if(ex instanceof AllergyNotFoundException allergyNotFoundException){
            logger.warn("Invalid allergy IDs supplied. for user={}", allergyNotFoundException.getEmail());
        }
        else {
            logger.error("Unhandled exception", ex);
        }

    }
}
