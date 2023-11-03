package org.example.сonfiguration;

import org.springframework.context.annotation.*;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletException;

/**
 * The {@code MyConfig} class is a configuration class for setting up a Spring MVC web application.
 *
 * <p>This class is annotated with `@Configuration`, which indicates that it provides bean definitions.
 * Additionally, it is annotated with `@EnableWebMvc` to enable Spring Web MVC support, `@EnableAspectJAutoProxy` to enable support for aspect-oriented programming (AOP),
 * and `@ComponentScan` to specify the base package(s) for component scanning.
 *
 * <p>It also implements the {@link WebApplicationInitializer} interface, allowing it to configure the Servlet 3.0+ web application without using web.xml.
 *
 * <p>Inside the {@link #onStartup(ServletContext)} method, it sets up an {@link AnnotationConfigWebApplicationContext} and registers itself as a configuration class.
 * It also registers a {@link ContextLoaderListener} and a {@link DispatcherServlet} for handling HTTP requests.
 *
 * <p>Example usage:
 * <pre>
 * // This class is typically used to configure your Spring MVC web application.
 * public class MyWebAppInitializer implements WebApplicationInitializer {
 *     public void onStartup(ServletContext servletContext) throws ServletException {
 *         AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
 *         context.register(MyConfig.class);
 *
 *         servletContext.addListener(new ContextLoaderListener(context));
 *
 *         ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
 *         dispatcher.setLoadOnStartup(1);
 *         dispatcher.addMapping("/");
 *     }
 * }
 * </pre>
 */
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan("org.example")
public class MyConfig implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(MyConfig.class);

        servletContext.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}

