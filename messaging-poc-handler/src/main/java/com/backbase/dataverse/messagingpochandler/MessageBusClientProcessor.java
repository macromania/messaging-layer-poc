package com.backbase.dataverse.messagingpochandler;

import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;

@Slf4j
@Service
/**
 * Implementing SmartLifecycle to start and stop the processor client.
 * https://github.com/Azure/azure-sdk-for-java/issues/29997
 */
public class MessageBusClientProcessor implements SmartLifecycle {

    private final ServiceBusProcessorClient serviceBusProcessorClient;
    private boolean running;

    public MessageBusClientProcessor(MessageBusClientBuilder messageBusClientBuilder) {
        log.info("Creating MessageBusClientProcessor");
        this.serviceBusProcessorClient = messageBusClientBuilder.build(MessageBusClientProcessor::processMessage, MessageBusClientProcessor::processError);
    }

    private static void processMessage(ServiceBusReceivedMessageContext context) {
        ServiceBusReceivedMessage message = context.getMessage();
        log.info("Processing message. Id: {}, Sequence #: {}. Contents: {}", message.getMessageId(), message.getSequenceNumber(), message.getBody());
        log.info("Completing context, deleting message from queue");
        context.complete();
    }

    private static void processError(ServiceBusErrorContext context) {
        log.info("Error when receiving messages from namespace: '{}'. Entity: {}", context.getFullyQualifiedNamespace(), context.getEntityPath());
    }

    @PostConstruct
    public void startProcessor(){

    }

    @PreDestroy
    public void stopProcessor(){

    }

    @Override
    public void start() {
        log.info("Processor starting");
        serviceBusProcessorClient.start();
        log.info("Processor started");
        running = true;
    }

    @Override
    public void stop() {
        log.info("Processor closing");
        serviceBusProcessorClient.close();
        log.info("Processor closed");
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
