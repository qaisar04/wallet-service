package org.example.aspects;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
@Log4j
public class LoggableAspect {
    @Pointcut("within(@org.example.annotations.Loggable *) && execution(* *(..))")
    public void annotatedByLoggable() { }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("Calling method " + methodSignature.toShortString());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();

        stopWatch.stop();
        log.info("Execution of method " + methodSignature.toShortString() +
                 " finished. Execution time is " + (endTime - startTime) + " ms");
        return result;
    }

}


