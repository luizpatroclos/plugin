
package eu.domibus.ep.edelivery.plugin.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmitRequest implements Serializable
{
    @JsonIgnore
    private Bodyload bodyload;
    private List<Payload> payload = null;
    private static final long serialVersionUID = 2853478197207094372L;

    public SubmitRequest(Bodyload bodyload, List<Payload> payload) {
        this.bodyload = bodyload;
        this.payload = payload;
    }


    public SubmitRequest(){}

    public Bodyload getBodyload() {
        return bodyload;
    }

    public void setBodyload(Bodyload bodyload) {
        this.bodyload = bodyload;
    }

    public List<Payload> getPayload() {
        return payload;
    }

    public void setPayload(List<Payload> payload) {
        this.payload = payload;
    }

}
