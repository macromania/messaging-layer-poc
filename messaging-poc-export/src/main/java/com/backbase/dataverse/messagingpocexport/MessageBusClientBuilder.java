package com.backbase.dataverse.messagingpocexport;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClient;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClientBuilder;
import com.azure.messaging.servicebus.administration.implementation.ServiceBusManagementClientImpl;
import com.backbase.dataverse.messagingpocexport.config.AppConfig;
import org.springframework.stereotype.Service;

@Service
public class MessageBusClientBuilder {

    private final AppConfig appConfig;
    private ServiceBusSenderClient serviceBusSenderClient;
    private ServiceBusAdministrationClient serviceBusAdministrationClient;

    public MessageBusClientBuilder(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public ServiceBusSenderClient build() {
        if (serviceBusSenderClient != null) {
            return serviceBusSenderClient;
        }

        serviceBusSenderClient = new ServiceBusClientBuilder()
                .connectionString(appConfig.getServiceBus().getConnectionString())
                .sender()
                .queueName(appConfig.getServiceBus().getQueueName())
                .buildClient();

        return serviceBusSenderClient;
    }

    public ServiceBusAdministrationClient buildManagementClient() {
        if(serviceBusAdministrationClient != null) {
            return serviceBusAdministrationClient;
        }

        serviceBusAdministrationClient = new ServiceBusAdministrationClientBuilder()
                .connectionString(appConfig.getServiceBus().getConnectionString())
                .buildClient();

        return serviceBusAdministrationClient;
    }
}
