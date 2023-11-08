package org.example.logging.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Loggable} annotation is used to mark methods and classes that should be logged.
 * When applied to a class, it indicates that all methods within that class should be logged.
 * When applied to a method, it indicates that the method itself should be logged.
 * Logging behavior may include method entry and exit, as well as any additional information
 * specific to the application's requirements.
 *
 * <p>This annotation is typically used in combination with AOP (Aspect-Oriented Programming) to
 * intercept and log method calls automatically.
 *
 * <p>Example usages:
 * <pre>
 *   // Apply to a class to log all methods within the class
 *   @Loggable
 *   public class MyClass {
 *       // Class definition
 *   }
 *
 *   // Apply to a method to log that specific method
 *   public class AnotherClass {
 *       @Loggable
 *       public void myMethod() {
 *           // Method implementation
 *       }
 *   }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface LoggableTime {
}
