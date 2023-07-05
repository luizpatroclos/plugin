
package eu.domibus.ep.edelivery.plugin.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmitMessage implements Serializable
{

    private static final long serialVersionUID = 1399369461999509517L;
    private Messaging messaging;
    private SubmitRequest submitRequest;

    public SubmitMessage(Messaging messaging, SubmitRequest submitRequest) {
        this.messaging = messaging;
        this.submitRequest = submitRequest;
    }

    public SubmitMessage(Messaging messaging) {
        this.messaging = messaging;
    }

    public SubmitMessage(){}

    public Messaging getMessaging() {
        return messaging;
    }

    public void setMessaging(Messaging messaging) {
        this.messaging = messaging;
    }

    public SubmitRequest getSubmitRequest() {
        return submitRequest;
    }

    public void setSubmitRequest(SubmitRequest submitRequest) {
        this.submitRequest = submitRequest;
    }

}
