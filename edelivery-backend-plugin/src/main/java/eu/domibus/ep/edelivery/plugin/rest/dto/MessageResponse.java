package eu.domibus.ep.edelivery.plugin.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.List;



@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse implements Serializable {

    private static final long serialVersionUID = 2853478197482394372L;
    private List<Payload> payload = null;

    public MessageResponse(){}

    public MessageResponse(List<Payload> payload) {
        this.payload = payload;
    }

    public List<Payload> getPayload() {
        return payload;
    }
    public void setPayload(List<Payload> payload) {
        this.payload = payload;
    }
}
