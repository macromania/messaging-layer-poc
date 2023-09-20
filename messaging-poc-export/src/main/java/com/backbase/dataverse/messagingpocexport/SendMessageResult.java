package com.backbase.dataverse.messagingpocexport;

import lombok.Builder;

@Builder
public class SendMessageResult {

    private String message;
    private boolean success;
}
