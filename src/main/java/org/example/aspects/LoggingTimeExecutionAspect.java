package org.example.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LoggingTimeExecutionAspect {

    @Around("execution(* org.example.manager..*.*(..))")
    public Object logMethodExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        final var stopWatch = new org.apache.commons.lang3.time.StopWatch();

        stopWatch.start();
        var result = pjp.proceed();
        stopWatch.stop();
        System.out.println("__ __ __ __ __ __ __ __ __ __ __");
        System.out.println(
                "%s.%s :: %s ms".formatted(
                        methodSignature.getDeclaringType().getSimpleName(), // class name
                        methodSignature.getName(), // method name
                        stopWatch.getTime(TimeUnit.MILLISECONDS) // execution time in milliseconds
                )
        );
        System.out.println("__ __ __ __ __ __ __ __ __ __ __");

        return result;
    }
}
