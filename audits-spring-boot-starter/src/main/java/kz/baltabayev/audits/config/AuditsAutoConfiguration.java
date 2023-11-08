package kz.baltabayev.audits.config;

import jakarta.annotation.PostConstruct;
import kz.baltabayev.audits.aspects.AuditAspect;
import kz.baltabayev.audits.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableConfigurationProperties(AuditProperties.class)
@ConditionalOnClass(AuditProperties.class)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.common.audits", name = "enabled", havingValue = "true")
@EnableAspectJAutoProxy
public class AuditsAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(AuditsAutoConfiguration.class);

    @PostConstruct
    void init() {
        logger.info("AuditsAutoConfiguration initialized");
    }

    @Bean
    @ConditionalOnClass(AuditAspect.class)
    public AuditAspect auditAspect() {
        logger.info("AuditAspect bean start to create.");
        return new AuditAspect(new AuditService());
    }
}
