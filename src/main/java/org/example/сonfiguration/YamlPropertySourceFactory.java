package org.example.сonfiguration;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * The {@code YamlPropertySourceFactory} class is a custom Spring Factory class used to load YAML properties files as PropertySource objects.
 *
 * <p>YAML (YAML Ain't Markup Language) is a human-readable data serialization format. This class allows you to load YAML properties files and treat them as PropertySource objects, making it possible to use them in Spring's property configuration.
 *
 * <p>By implementing the `PropertySourceFactory` interface, this class can be used to load YAML properties files and convert them into Spring PropertySource objects.
 *
 * <p>Example usage:
 * <pre>
 * // This class is used to load YAML properties files and treat them as PropertySource objects in a Spring application.
 * @PropertySource(value = "classpath:my-config.yml", factory = YamlPropertySourceFactory.class)
 * public class MyConfig {
 *     // ...
 * }
 * </pre>
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource)
            throws IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(encodedResource.getResource());
        Properties props = factory.getObject();
        return new PropertiesPropertySource(encodedResource.getResource().getFilename(), props);
    }
}
