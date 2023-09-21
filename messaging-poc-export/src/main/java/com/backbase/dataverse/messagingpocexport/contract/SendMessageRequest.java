package com.backbase.dataverse.messagingpocexport.contract;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
public class SendMessageRequest {
    String message;
    String sessionId;
}
