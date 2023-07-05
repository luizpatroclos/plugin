
package eu.domibus.ep.edelivery.plugin.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMessage implements Serializable
{

    private static final long serialVersionUID = 6529377077704264933L;
    private String mpc;
    private MessageInfo messageInfo;
    private PartyInfo partyInfo;
    private CollaborationInfo collaborationInfo;
    private MessageProperty messageProperties = null;
    private PayloadInfo payloadInfo;

    public String getMpc() {
        return mpc;
    }

    public void setMpc(String mpc) {
        this.mpc = mpc;
    }

    public MessageInfo getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(MessageInfo messageInfo) {
        this.messageInfo = messageInfo;
    }

    public PartyInfo getPartyInfo() {
        return partyInfo;
    }

    public void setPartyInfo(PartyInfo partyInfo) {
        this.partyInfo = partyInfo;
    }

    public CollaborationInfo getCollaborationInfo() {
        return collaborationInfo;
    }

    public void setCollaborationInfo(CollaborationInfo collaborationInfo) {
        this.collaborationInfo = collaborationInfo;
    }

    public MessageProperty getMessageProperties() {
        return messageProperties;
    }

    public void setMessageProperties(MessageProperty messageProperties) {
        this.messageProperties = messageProperties;
    }

    public PayloadInfo getPayloadInfo() {
        return payloadInfo;
    }

    public void setPayloadInfo(PayloadInfo payloadInfo) {
        this.payloadInfo = payloadInfo;
    }

}
