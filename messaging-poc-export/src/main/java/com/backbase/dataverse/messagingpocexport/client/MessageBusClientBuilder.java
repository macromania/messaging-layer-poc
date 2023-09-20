package com.backbase.dataverse.messagingpocexport.client;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.backbase.dataverse.messagingpocexport.config.ServiceBusConfig;
import org.springframework.stereotype.Service;

@Service
public class MessageBusClientBuilder {

    private final ServiceBusConfig serviceBusConfig;

    public MessageBusClientBuilder(ServiceBusConfig serviceBusConfig) {
        this.serviceBusConfig = serviceBusConfig;
    }

    public ServiceBusSenderClient build() {
        return new ServiceBusClientBuilder()
                .connectionString(serviceBusConfig.getConnectionString())
                .sender()
                .queueName(serviceBusConfig.getQueueName())
                .buildClient();
    }
}
