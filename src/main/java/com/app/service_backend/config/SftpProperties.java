package com.app.service_backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sftp")
public class SftpProperties {

    private String username;
    private String password;
    private String host;
    private String port;
    private String folder;
    private String moveFolder;
}
