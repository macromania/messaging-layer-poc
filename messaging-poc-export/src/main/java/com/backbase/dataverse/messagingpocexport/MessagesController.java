package com.backbase.dataverse.messagingpocexport;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClient;
import com.backbase.dataverse.messagingpocexport.config.AppConfig;
import com.backbase.dataverse.messagingpocexport.config.MessageBusConfig;
import com.backbase.dataverse.messagingpocexport.contract.SendMessageRequest;
import com.backbase.dataverse.messagingpocexport.contract.SendMessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class MessagesController {

    private final ServiceBusSenderClient serviceBusSenderClient;
    private final ServiceBusAdministrationClient serviceBusAdministrationClient;
    private final MessageBusConfig messageBusConfig;


    public MessagesController(MessageBusClientBuilder messageBusClientBuilder, AppConfig appConfig) {
        this.serviceBusSenderClient = messageBusClientBuilder.build();
        this.serviceBusAdministrationClient = messageBusClientBuilder.buildManagementClient();
        this.messageBusConfig = appConfig.getServiceBus();
    }

    @PostMapping("/messages")
    public ResponseEntity<SendMessageResult> sendMessage(@RequestBody SendMessageRequest request) {
        log.info("Sending message: {} with session: {}", request.getMessage(), request.getSessionId());

        try {
            var serviceBusMessage = new ServiceBusMessage(request.getMessage());
            serviceBusMessage.setSessionId(request.getSessionId());

            serviceBusSenderClient.sendMessage(serviceBusMessage);
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
            var error = SendMessageResult.builder()
                    .message(e.getMessage() + ": Timestamp: " + System.currentTimeMillis())
                    .sessionId(request.getSessionId())
                    .success(false)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        var result = SendMessageResult.builder()
                .message(request.getMessage())
                .success(true)
                .build();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/messages/count")
    public ResponseEntity<String> getMessage() {
        log.info("Getting message count of the queue {}", messageBusConfig.getQueueName());

        try {
            var messageCount = serviceBusAdministrationClient.getQueueRuntimeProperties(messageBusConfig.getQueueName()).getActiveMessageCount();
            return ResponseEntity.ok("Message count: " + messageCount);
        }
        catch(Exception e) {
            log.error("Error getting message count: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
