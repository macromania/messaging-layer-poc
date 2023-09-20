package com.backbase.dataverse.messagingpocexport;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@Getter
public class MessageBusConfig {

    private String connectionString;
    private String queueName;

}
