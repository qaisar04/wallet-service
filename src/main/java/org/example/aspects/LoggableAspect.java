package org.example.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggableAspect {

    @Pointcut("within(@org.example.annotations.Loggable *) && execution(* *(..))")
    public void annotatedByLoggable() { }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        final org.apache.commons.lang3.time.StopWatch stopWatch = new org.apache.commons.lang3.time.StopWatch();
        stopWatch.start();

        System.out.println("__ __ __ __ __ __ __ __ __ __ __");
        System.out.println("Calling method " + methodSignature.toShortString());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();

        stopWatch.stop();
        System.out.println("Execution of method " + methodSignature.toShortString() +
                 " finished. Execution time is " + (endTime - startTime) + " ms");
        System.out.println("__ __ __ __ __ __ __ __ __ __ __");
        return result;
    }

}

