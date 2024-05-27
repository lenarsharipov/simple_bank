package com.lenarsharipov.simplebank.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

//    @Around("execution(* com.lenarsharipov.simplebank..*.*(..))")
    @Around("execution(* com.lenarsharipov.simplebank..*.*(..)) && !within(com.lenarsharipov.simplebank.exception.ControllerAdvice)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("{} method execution start", joinPoint.getSignature().toString());
        Object returnObj = joinPoint.proceed();
        log.info("{} method execution end", joinPoint.getSignature().toString());
        return returnObj;
    }

    @AfterThrowing(value = "execution(* com.lenarsharipov.simplebank..*(..))",throwing = "e")
    public void logException(JoinPoint joinPoint, Exception e) {
        log.warn("{} An exception happened due to : {}", joinPoint.getSignature(), e.getMessage());
    }
}
