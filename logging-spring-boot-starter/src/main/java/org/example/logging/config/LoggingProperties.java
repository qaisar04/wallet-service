package org.example.logging.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "app.common.logging")
public class LoggingProperties {
    /**
     * to enable common logging aop on service layer
     */
    private boolean enabled;
    private String level;

    public static final Logger logger = LoggerFactory.getLogger(LoggingProperties.class);

    @PostConstruct
    void init() {
        logger.info("Logging properties initialized: {}", this);
    }
}
