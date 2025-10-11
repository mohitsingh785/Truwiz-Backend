package org.Jtech.aop;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {


    @Pointcut("execution(* org.Jtech.Service.HealthCheckService.getallcategory(..))")
    public void getAllCategoryLog(){}




    @Before("getAllCategoryLog()")
    public void logBeforegetAllCategory() {
        System.out.println("getAllCategory() is about to be called");
    }
}
