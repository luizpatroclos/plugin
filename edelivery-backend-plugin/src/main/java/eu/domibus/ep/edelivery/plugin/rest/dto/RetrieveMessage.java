
package eu.domibus.ep.edelivery.plugin.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"messaging", "messageResponse"})
public class RetrieveMessage implements Serializable
{

    private static final long serialVersionUID = 1399369461889509517L;
    private Messaging messaging;
    private MessageResponse messageResponse;

    public RetrieveMessage(Messaging messaging, MessageResponse messageResponse) {
        this.messaging = messaging;
        this.messageResponse = messageResponse;
    }

    public RetrieveMessage(Messaging messaging) {
        this.messaging = messaging;
    }

    public RetrieveMessage(){}

    public Messaging getMessaging() {
        return messaging;
    }

    public void setMessaging(Messaging messaging) {
        this.messaging = messaging;
    }

    public MessageResponse getMessageResponse() {
        return messageResponse;
    }

    public void setMessageResponse(MessageResponse messageResponse) {
        this.messageResponse = messageResponse;
    }

}
