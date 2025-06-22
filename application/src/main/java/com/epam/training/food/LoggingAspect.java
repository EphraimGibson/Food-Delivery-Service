package com.epam.training.food;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);


    @Before("@annotation(com.epam.training.food.aspect.EnableArgumentLogging)")
    public void logMethodParameters(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        LOGGER.info("Method name: [{}], parameter(s): {}", methodName, args);
    }

    @AfterReturning(pointcut = "@annotation(com.epam.training.food.aspect.EnableReturnValueLogging)", returning = "result")
    public void loggingReturnValue(JoinPoint joinPoint, Object result){
        String methodName = joinPoint.getSignature().getName();
        LOGGER.info("Method name: [{}], return value: {}", methodName , result);
    }

    @Around("@annotation(com.epam.training.food.aspect.EnableExecutionTimeLogging)")
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        String methodName = proceedingJoinPoint.getSignature().getName();
        LOGGER.info("Method name: [{}], Execution time: {}ms", methodName, duration);

        return result;
    }

}
