package org.example.aspects;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * The {@code LoggingTimeExecutionAspect} class is an AspectJ aspect responsible for logging the execution time of
 * methods within the `org.example.manager` package. It measures the time taken by method executions in milliseconds
 * and logs this information along with the class and method name.
 *
 * <p>This aspect is configured to log method execution times for all classes and methods within the `org.example.manager`
 * package. It is designed to work in conjunction with Spring AOP and provides a convenient way to measure and log
 * method execution times for the specified package.
 *
 * <p>Example usage:
 * <pre>
 * // Add this aspect to your Spring application context
 * &lt;context:component-scan base-package="org.example.aspects" /&gt;
 *
 * // Any methods in the 'org.example.manager' package will have their execution times automatically logged.
 * </pre>
 *
 * When methods within the `org.example.manager` package are invoked, this aspect will measure and log the time taken
 * for their execution. The aspect can be enabled or disabled globally by enabling or disabling component scanning
 * of the package where this aspect is defined.
 */
@Aspect
@Log4j
@Component
public class LoggingTimeExecutionAspect {

    /**
     * Advice method that intercepts method calls in the 'org.example.manager' package, measures the execution time,
     * and logs the method's class and method name along with the execution time.
     *
     * @param pjp The join point representing the intercepted method call.
     * @return The result of the intercepted method call.
     * @throws Throwable If an error occurs during method execution.
     */
    @Around("execution(* org.example.manager..*.*(..))")
    public Object logMethodExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        final var stopWatch = new StopWatch();

        stopWatch.start();
        var result = pjp.proceed();
        stopWatch.stop();

        log.info("__ __ __ __ __ __ __ __ __ __ __");
        log.info(
                "%s.%s :: %s ms".formatted(
                        methodSignature.getDeclaringType().getSimpleName(), // class name
                        methodSignature.getName(), // method name
                        stopWatch.getTime(TimeUnit.MILLISECONDS) // execution time in milliseconds
                )
        );
        log.info("__ __ __ __ __ __ __ __ __ __ __");

        return result;
    }
}
