package org.example.logging.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.logging.aop.annotations.LoggableInfo;
import org.example.logging.aop.annotations.LoggableTime;
import org.example.logging.aop.aspects.LoggingMethodExecutionAspect;
import org.example.logging.aop.aspects.LoggingTimeExecutionAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnClass(LoggingProperties.class)
@ConditionalOnProperty(prefix = "app.common.logging", name = "enabled", havingValue = "true")
@EnableAspectJAutoProxy
public class LoggingAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAutoConfiguration.class);

    @PostConstruct
    void init() {
        logger.info("LoggingAutoConfiguration initialized");
    }

    @Bean
    @ConditionalOnClass(LoggingMethodExecutionAspect.class)
    public LoggingMethodExecutionAspect loggingMethodExecutionAspect() {
        logger.info("LoggingMethodExecutionAspect bean start to create.");
        return new LoggingMethodExecutionAspect();
    }

    @Bean
    @ConditionalOnClass(LoggingTimeExecutionAspect.class)
    public LoggingTimeExecutionAspect loggingTimeExecutionAspect() {
        logger.info("LoggingTimeExecutionAspect bean start to create.");
        return new LoggingTimeExecutionAspect();
    }
}

