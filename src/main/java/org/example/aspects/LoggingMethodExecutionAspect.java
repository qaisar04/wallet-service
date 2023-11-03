package org.example.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingMethodExecutionAspect {

    @Around("execution(* org.example.manager..*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String methodName = methodSignature.getName();
        String className = methodSignature.getDeclaringType().getSimpleName();
        System.out.println("__ __ __ __ __ __ __ __ __ __ __");
        System.out.println("Executing method " + methodName + " in class " + className);

        long startTime = System.currentTimeMillis();
        Object result = pjp.proceed();
        long endTime = System.currentTimeMillis();

        System.out.println("Method " + methodName + " in class " + className + " completed in " + (endTime - startTime) + " ms.");
        System.out.println("__ __ __ __ __ __ __ __ __ __ __");

        return result;
    }
}