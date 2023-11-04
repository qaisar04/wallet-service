package org.example.aspects;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * The {@code LoggableAspect} class is an AspectJ aspect responsible for logging method executions
 * of classes or methods annotated with the {@link org.example.annotations.Loggable} annotation.
 *
 * <p>This aspect intercepts method calls and logs information about the method execution, including
 * the method name, execution time, and a message indicating the start and end of execution.
 * It is designed to work in conjunction with Spring AOP.
 *
 * <p>Example usage:
 * <pre>
 * // Annotate a class or method with @Loggable
 * @Loggable
 * public class MyService {
 *     public void doSomething() {
 *         // Method implementation
 *     }
 * }
 * </pre>
 *
 * When methods within the annotated class are invoked, this aspect will log information about their execution.
 * The aspect can be enabled or disabled globally by enabling or disabling component scanning of the package
 * where this aspect is defined.
 */
@Component
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

        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();

        stopWatch.stop();

        log.info("__ __ __ __ __ __ __ __ __ __ __");
        log.info("Calling method " + methodSignature.toShortString());
        log.info("Execution of method " + methodSignature.toShortString() +
                 " finished. Execution time is " + (endTime - startTime) + " ms");
        log.info("__ __ __ __ __ __ __ __ __ __ __");

        return result;
    }

}

