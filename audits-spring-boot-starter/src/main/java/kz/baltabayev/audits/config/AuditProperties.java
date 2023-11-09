package kz.baltabayev.audits.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "app.common.audits")
public class AuditProperties {

    /**
     * to enable common logging aop on service layer
     */
    private boolean enabled;
    private String level;

    public static final Logger logger = LoggerFactory.getLogger(AuditProperties.class);

    @PostConstruct
    void init() {
        logger.info("Audit properties initialized: {}", this);
    }
}
