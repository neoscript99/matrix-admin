package com.feathermind.matrix.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties({MatrixConfigProperties.class})
@ConfigurationProperties(prefix = "matrix")
public class MatrixConfigProperties {
    private String defaultRoles;
    private boolean casClientEnabled = false;
    private long tokenExpireMinutes;
    private String kkFileViewRoot = "http://localhost:8012";
    /**
     * 支持内网访问
     */
    private String kkFileVieDownloadUrl = "http://localhost:8080/download";
}
