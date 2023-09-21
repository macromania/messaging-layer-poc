package com.backbase.dataverse.messagingpochandler;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClient;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClientBuilder;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.backbase.dataverse.messagingpochandler.config.AppConfig;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class MessageBusClientBuilder {

    private final AppConfig appConfig;
    private ServiceBusProcessorClient serviceBusProcessorClient;
    private ServiceBusAdministrationClient serviceBusAdministrationClient;

    public MessageBusClientBuilder(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public ServiceBusProcessorClient build(Consumer<ServiceBusReceivedMessageContext> processMessage, Consumer<ServiceBusErrorContext> processError) {
        if (serviceBusProcessorClient != null) {
            return serviceBusProcessorClient;
        }

        serviceBusProcessorClient = new ServiceBusClientBuilder()
                .connectionString(appConfig.getServiceBus().getConnectionString())
                .processor()
                .receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
                .queueName(appConfig.getServiceBus().getQueueName())
                .processMessage(processMessage)
                .processError(processError)
                .disableAutoComplete() // Make sure to explicitly opt in to manual settlement (e.g. complete, abandon).
                .buildProcessorClient();

        return serviceBusProcessorClient;
    }
}
