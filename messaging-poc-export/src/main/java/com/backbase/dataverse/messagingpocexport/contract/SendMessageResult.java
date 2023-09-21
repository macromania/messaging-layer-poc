package com.backbase.dataverse.messagingpocexport.contract;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SendMessageResult {
    String message;
    String sessionId;
    boolean success;
}
