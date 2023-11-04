package org.example.сonfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * The {@code WebConfig} class is a configuration class for customizing web-related configurations in a Spring application.
 *
 * <p>This class is annotated with `@Configuration`, indicating that it provides bean definitions for custom configurations.
 *
 * <p>It implements the `WebMvcConfigurer` interface, allowing for custom configuration of web-based features in a Spring application.
 *
 * <p>In this specific configuration, the class customizes message converters used for JSON serialization to enable pretty-printing (indenting) of JSON responses.
 *
 * <p>Example usage:
 * <pre>
 * // This class is typically used to configure custom web-related settings in your Spring application.
 * public class MyWebInitializer {
 *     public static void main(String[] args) {
 *         SpringApplication.run(WebConfig.class, args);
 *     }
 * }
 * </pre>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }
}