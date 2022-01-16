package com.skillbox.socialnet.util.anotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogResultAspect {

    public static final Logger logger = LogManager.getLogger("methodReturn");

    ObjectMapper mapper = new ObjectMapper();

    @Around("@annotation(LogResult)")
    public Object loggingResultOfMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        String prettyResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        logger.info(String.format("%s\n%s", joinPoint.getSignature(), prettyResult));
        return result;
    }
}
