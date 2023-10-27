package org.example.aspects;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Log4j
public class LoggableAspect {
    @Pointcut("within(@org.example.annotations.Loggable *) && execution(* *(..))")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Calling method " + proceedingJoinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Execution of method " + proceedingJoinPoint.getSignature() +
                " finished. Execution time is " + (endTime - startTime) + " ms");
        return result;
    }

}
