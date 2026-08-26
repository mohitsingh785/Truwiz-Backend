package org.Jtech.aop;


import org.Jtech.Exception.InvalidCredentialsException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * Logging Aspect
 *
 * Purpose:
 * Provides cross-cutting logging functionality for service-layer
 * methods to track method invocation flow.
 *
 * Scope:
 * - Logs execution of selected service methods
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
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
    public void logException(Throwable ex){

        if (ex instanceof InvalidCredentialsException invalidCredentialsException){
            logger.warn("Invalid login attempt for email: {}",invalidCredentialsException.getEmail());
        }

    }
}
