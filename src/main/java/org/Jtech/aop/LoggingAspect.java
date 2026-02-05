package org.Jtech.aop;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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
public class LoggingAspect {


}
