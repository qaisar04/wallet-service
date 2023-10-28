package org.example.aspects;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

@Aspect
@Log4j
public class LoggingMethodExecutionAspect {

    @Around("execution(* org.example.manager..*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String methodName = methodSignature.getName();
        String className = methodSignature.getDeclaringType().getSimpleName();
        Object[] args = pjp.getArgs();

        log.info("Executing method " + methodName + " in class " + className + " with arguments: " + Arrays.toString(args));

        long startTime = System.currentTimeMillis();
        Object result = pjp.proceed();
        long endTime = System.currentTimeMillis();

        log.info("Method " + methodName + " in class " + className + " completed in " + (endTime - startTime) + " ms. Returned: " + result);

        return result;
    }
}
