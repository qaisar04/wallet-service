package org.example.aspects;

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

