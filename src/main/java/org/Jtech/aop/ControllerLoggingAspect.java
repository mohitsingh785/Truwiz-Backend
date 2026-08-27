package org.Jtech.aop;


import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.annotations.CollectionIdMutability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLoggingAspect {
    private static final Logger logger =
            LoggerFactory.getLogger(ControllerLoggingAspect.class);

    private final HttpServletRequest request;

    public ControllerLoggingAspect(HttpServletRequest request) {
        this.request = request;
    }
    @Pointcut("execution(* org.Jtech.Controller..*(..))")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logHttpRequest(ProceedingJoinPoint joinPoint) throws Throwable {

        String method = request.getMethod();
        String uri = request.getRequestURI();

        logger.info("Incoming {} {}", method, uri);

        long startTime = System.currentTimeMillis();

        try {

            Object result = joinPoint.proceed();

            long timeTaken = System.currentTimeMillis() - startTime;

            if (result instanceof
                    ResponseEntity<?> response) {
                logger.info(
                        "Completed {} {} {} {} in {} ms",
                        method,
                        uri,
                        response.getStatusCode().value(),
                        response.getStatusCode(),
                        timeTaken
                );
            } else {
                logger.info(
                        "Completed {} {} in {} ms",
                        method,
                        uri,
                        timeTaken
                );
            }

            return result;

        } catch (Throwable ex) {

            long timeTaken = System.currentTimeMillis() - startTime;

            logger.error(
                    "Failed {} {} after {} ms",
                    method,
                    uri,
                    timeTaken
            );

            throw ex;
        }}
}
