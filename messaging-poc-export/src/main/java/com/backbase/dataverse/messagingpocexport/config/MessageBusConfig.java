package com.backbase.dataverse.messagingpocexport.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageBusConfig {
    private String connectionString;
    private String queueName;
}
