package com.backbase.dataverse.messagingpocexport;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.stereotype.Service;

@Service
public class MessageBusClientBuilder {

    private final MessageBusConfig messageBusConfig;

    public MessageBusClientBuilder(MessageBusConfig messageBusConfig) {
        this.messageBusConfig = messageBusConfig;
    }

    public ServiceBusSenderClient build() {
        return new ServiceBusClientBuilder()
                .connectionString(messageBusConfig.getConnectionString())
                .sender()
                .queueName(messageBusConfig.getQueueName())
                .buildClient();
    }
}
