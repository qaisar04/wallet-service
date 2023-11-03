package org.example.сonfiguration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * The {@code DataSourceConfiguration} class is responsible for configuring the data source used by the application.
 *
 * <p>It defines a data source bean that provides the necessary configuration for connecting to the database. Additionally, it can be
 * extended to configure database migration tools like SpringLiquibase.
 *
 * <p>Example usage:
 * <pre>
 * // Create an instance of JdbcTemplate in your Spring application.
 * &#64;Autowired
 * private JdbcTemplate jdbcTemplate;
 * </pre>
 *
 * The class is annotated with `@Configuration` to indicate that it provides bean definitions.
 * It is also annotated with `@PropertySource` to specify the location of the property source (application.yml).
 */
@Configuration
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class DataSourceConfiguration {

    // TODO: create bean SpringLiquibase

    @Value("${db.url}")
    private String url;
    @Value("${db.driver}")
    private String driver;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;

    /**
     * Creates a {@link JdbcTemplate} bean configured with the data source properties.
     *
     * @return A JdbcTemplate configured with the specified data source.
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return new JdbcTemplate(dataSource);
    }
}

