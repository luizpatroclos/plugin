
package eu.domibus.ep.edelivery.plugin.rest.dto;

import java.io.Serializable;

public class Payload implements Serializable
{

    private static final long serialVersionUID = 7602305965335019448L;
    private String payloadId;
    private String contentType;
    private byte[] value;

    public String getPayloadId() {
        return payloadId;
    }

    public void setPayloadId(String payloadId) {
        this.payloadId = payloadId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
