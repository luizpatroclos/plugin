
package eu.domibus.ep.edelivery.plugin.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaborationInfo implements Serializable
{

    private static final long serialVersionUID = -546348145256535985L;
    private String action;
    private String conversationId;
    private AgreementRef agreementRef;
    private Service service;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public AgreementRef getAgreementRef() {
        return agreementRef;
    }

    public void setAgreementRef(AgreementRef agreementRef) {
        this.agreementRef = agreementRef;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

}
