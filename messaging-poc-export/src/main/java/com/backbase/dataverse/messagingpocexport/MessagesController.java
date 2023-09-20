package com.backbase.dataverse.messagingpocexport;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@Slf4j
public class MessagesController {

    private final ServiceBusSenderClient serviceBusSenderClient;


    public MessagesController(MessageBusClientBuilder messageBusClientBuilder) {
        this.serviceBusSenderClient = messageBusClientBuilder.build();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/messages")
    public SendMessageResult sendMessage(String message) {
        log.info("Sending message: {}", message);

        try(serviceBusSenderClient) {
            serviceBusSenderClient.sendMessage(new ServiceBusMessage(message));
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
            return SendMessageResult.builder()
                    .message(e.getMessage())
                    .success(false)
                    .build();
        }

        return SendMessageResult.builder()
                .message(message)
                .success(true)
                .build();
    }
}
