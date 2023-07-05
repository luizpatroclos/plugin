package eu.domibus.ep.edelivery.plugin.rest.dto;

import java.io.Serializable;

public class MessageStatusResponse implements Serializable {

    private MessageStatus status;

    public MessageStatus getStatus() {
        return status;
    }
    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}
