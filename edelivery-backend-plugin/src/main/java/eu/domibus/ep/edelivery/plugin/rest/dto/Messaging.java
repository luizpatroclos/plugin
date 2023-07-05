
package eu.domibus.ep.edelivery.plugin.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Messaging implements Serializable
{
    private static final long serialVersionUID = 2345823247616836125L;
    private String mustUnderstand;
    private UserMessage userMessage;

    public Messaging(String mustUnderstand, UserMessage userMessage) {
        this.mustUnderstand = mustUnderstand;
        this.userMessage = userMessage;
    }
    public Messaging(){}

    public String getMustUnderstand() {
        return mustUnderstand;
    }

    public void setMustUnderstand(String mustUnderstand) {
        this.mustUnderstand = mustUnderstand;
    }

    public UserMessage getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(UserMessage userMessage) {
        this.userMessage = userMessage;
    }

}
