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
    private boolean filePublic = false;
    private boolean filePreviewEnable = false;
    private String filePreviewRoot = "http://localhost:8012";

    /**
     * 不下载图片，如果是外网环境，要配置为外网域名
     */
    private String fileDownloadRoot = "http://localhost:8080";
    private boolean devLogin = true;
}
