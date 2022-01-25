package com.skillbox.socialnet.util.anotation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogResultAspect {

    public static final Logger logger = LogManager.getLogger("methodReturn");

    @Around("@annotation(MethodLog)")
    public Object loggingResultOfMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        String args = Arrays.toString(joinPoint.getArgs());
        logger.info("{}\n{}\n{}\n", joinPoint.getSignature(), args, result);
        return result;
    }
}
