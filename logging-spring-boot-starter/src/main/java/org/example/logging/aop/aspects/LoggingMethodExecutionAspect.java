package org.example.logging.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * The {@code LoggingMethodExecutionAspect} class is an AspectJ aspect responsible for logging method executions
 * in the `org.example.manager` package. It logs information about the start and end of method execution,
 * along with the execution time in milliseconds.
 *
 * <p>This aspect is configured to log method executions for all classes and methods within the `org.example.manager`
 * package. It is designed to work in conjunction with Spring AOP and provides a convenient way to log method
 * execution details for the specified package.
 *
 * <p>Example usage:
 * <pre>
 * // Add this aspect to your Spring application context
 * &lt;context:component-scan base-package="org.example.aspects" /&gt;
 *
 * // Any methods in the 'org.example.manager' package will be automatically logged.
 * </pre>
 * <p>
 * When methods within the `org.example.manager` package are invoked, this aspect will log information about their execution.
 * The aspect can be enabled or disabled globally by enabling or disabling component scanning of the package
 * where this aspect is defined.
 */
@Aspect
public class LoggingMethodExecutionAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingMethodExecutionAspect.class);


    @Pointcut("within(@org.example.logging.aop.annotations.LoggableInfo *) && execution(* *(..))")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object logMethodExecution(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String methodName = methodSignature.getName();
        String className = methodSignature.getDeclaringType().getSimpleName();

        logger.info("Executing method " + methodName + " in class " + className);

        long startTime = System.currentTimeMillis();
        Object result = pjp.proceed();
        long endTime = System.currentTimeMillis();

        logger.info("Method " + methodName + " in class " + className + " completed in " + (endTime - startTime) + " ms.");

        return result;
    }
}