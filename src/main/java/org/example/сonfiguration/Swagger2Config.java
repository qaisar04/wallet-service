package org.example.сonfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The {@code Swagger2Config} class is a configuration class for setting up Swagger documentation and UI in a Spring application.
 *
 * <p>This class is annotated with `@Configuration` and `@EnableSwagger2`, indicating that it provides bean definitions and enables Swagger support.
 *
 * <p>It configures Swagger to generate documentation for all endpoints (`RequestHandlerSelectors.any()`) and paths (`PathSelectors.any()`).
 *
 * <p>It also provides custom resource handling and view controller mapping for Swagger UI to be accessible at a specific URL.
 *
 * <p>Example usage:
 * <pre>
 * // This class is typically used to configure Swagger documentation in your Spring application.
 * public class MySwaggerInitializer {
 *     public static void main(String[] args) {
 *         SpringApplication.run(Swagger2Config.class, args);
 *     }
 * }
 * </pre>
 */
@EnableSwagger2
@Configuration
public class Swagger2Config implements WebMvcConfigurer {

//     http://localhost:8080/swagger-ui/index.html
//     http://localhost:8080/v2/api-docs

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.
                addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/")
                .setViewName("forward:" + "/swagger-ui/index.html");
    }

}