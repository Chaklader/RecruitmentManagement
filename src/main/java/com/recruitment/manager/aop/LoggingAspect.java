package com.recruitment.manager.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by Chaklader on Feb, 2021
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {


    /**
     * Pointcut that matches all repositories, services and Web REST endpoints. Method
     * is empty as this is just a Pointcut, the implementations are in the advices.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
                  " || within(@org.springframework.stereotype.Service *)" +
                  " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {

        log.info("#####################################");
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages. Method
     * is empty as this is just a Pointcut, the implementations are in the advices.
     */
    @Pointcut("within(com.recruitment.manager..*)" +
                  " || within(com.recruitment.manager.service..*)" +
                  " || within(com.recruitment.manager.api..*)")
    public void applicationPackagePointcut() {

        log.info("#####################################");
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {

        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }

        try {

            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}

