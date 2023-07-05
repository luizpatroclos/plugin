
package eu.domibus.ep.edelivery.plugin.rest.dto;

import java.io.Serializable;


public class Bodyload implements Serializable
{

    private String payloadId;
    private String contentType;
    protected byte[] value;
    private static final long serialVersionUID = -5687659406454974541L;

    public void setPayloadId(String payloadId) {
        this.payloadId = payloadId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPayloadId() {
        return payloadId;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

}
