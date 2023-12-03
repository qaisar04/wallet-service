package kz.baltabayev.audits.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableConfigurationProperties(AuditProperties.class)
@ConditionalOnClass(AuditProperties.class)
@ConditionalOnProperty(prefix = "app.common.audits", name = "enabled", havingValue = "true")
@EnableAspectJAutoProxy
@Configuration
public class AuditsAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(AuditsAutoConfiguration.class);

    @PostConstruct
    void init() {
        logger.info("AuditsAutoConfiguration initialized");
    }

}
