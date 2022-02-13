package com.skillbox.socialnet.util.annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogResultAspect {

    static final String CLASS_NAME_SEPARATOR = "\\$\\$";

    @Getter
    private final ConcurrentHashMap<String, Logger> loggerMap = new ConcurrentHashMap<>();

    @Pointcut("within(@com.skillbox.socialnet.util.annotation.InfoLoggable *)")
    public void infoLogClass() {
        //Need to be empty
    }

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {
        //Need to be empty
    }

    @Pointcut("execution(* *..*(..))")
    public void anyMethod() {
        //Need to be empty
    }

    @Pointcut("publicMethod() && infoLogClass()")
    public void infoLogPublicMethods() {
        //Need to be empty
    }

    @Pointcut("anyMethod() && infoLogClass()")
    public void infoLogAnyMethods() {
        //Need to be empty
    }

    @Before("infoLogAnyMethods()")
    public void logInfoStart(JoinPoint jp) {

        String className = className(jp);
        loggerMap.getOrDefault(className, log).info(startMessage(jp));
    }

    @AfterReturning(pointcut = "infoLogAnyMethods()", returning = "retVal")
    public void logInfoReturn(JoinPoint jp, Object retVal) {

        String className = className(jp);
        String message = endMessage(jp, retVal);
        if (message != null) {
            loggerMap.getOrDefault(className, log).info(message);
        }
    }

    String className(JoinPoint jp) {

        return jp.getTarget().getClass().getName().split(CLASS_NAME_SEPARATOR)[0];
    }

    private String startMessage(JoinPoint jp) {

        String methodName = jp.getSignature().getName();
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(methodName);
        logBuilder.append("(): start: ");
        if (jp.getArgs() != null && jp.getArgs().length != 0) {
            CodeSignature codeSignature = (CodeSignature) jp.getSignature();
            for (int i = 0; i < jp.getArgs().length; i++) {
                logBuilder.append(codeSignature.getParameterNames()[i]);
                logBuilder.append("=");
                logBuilder.append(jp.getArgs()[i]);
                if (jp.getArgs()[i] != jp.getArgs()[jp.getArgs().length - 1]) {
                    logBuilder.append(", ");
                }
            }
        }

        return logBuilder.toString();
    }

    private String endMessage(JoinPoint jp, Object retVal) {

        if (retVal != null) {
            String methodName = jp.getSignature().getName();

            return methodName + "(): end. Return = " + retVal;
        }

        return null;
    }
}
