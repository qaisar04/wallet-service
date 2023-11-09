package kz.baltabayev.audits.config;

import jakarta.annotation.PostConstruct;
import kz.baltabayev.audits.aspects.AuditAspect;
import kz.baltabayev.audits.service.AuditService;
import kz.baltabayev.audits.util.ConnectionManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;

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


    @Bean
    @Order(1)
    @ConditionalOnClass(ConnectionManager.class)
    public ConnectionManager connectionManager() {
        logger.info("ConnectionManager bean start to create.");
        return new ConnectionManager();
    }

    @Bean
    @Order(2)
    @ConditionalOnClass(AuditService.class)
    public AuditService auditService() {
        logger.info("AuditService bean start to create.");
        return new AuditService(connectionManager());
    }

    @Bean
    @Order(3)
    @ConditionalOnClass(AuditAspect.class)
    public AuditAspect auditAspect() {
        logger.info("AuditAspect bean start to create.");
        return new AuditAspect(auditService());
    }

}
