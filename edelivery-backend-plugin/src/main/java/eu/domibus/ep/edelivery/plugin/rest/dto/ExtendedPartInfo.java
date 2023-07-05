package eu.domibus.ep.edelivery.plugin.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.activation.DataHandler;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtendedPartInfo  extends PartInfo implements Serializable {

    protected byte[] binaryData;
    @JsonIgnore
    protected boolean inBody;
    @JsonIgnore
    protected transient DataHandler payloadDatahandler;

    public ExtendedPartInfo() {

    }

    public ExtendedPartInfo(PartInfo partInfo) {
        this.setHref(partInfo.getHref());
        this.setPartProperties(partInfo.getPartProperties());
    }

    public DataHandler getPayloadDatahandler() {

        if(binaryData != null){
            payloadDatahandler = new DataHandler(binaryData,"text/plain");
        }
        return payloadDatahandler;
    }

    public void setPayloadDatahandler(DataHandler payloadDatahandler) {
        this.payloadDatahandler = payloadDatahandler;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public boolean isInBody() {
        return inBody;
    }

    public void setInBody(boolean inBody) {
        this.inBody = inBody;
    }

}
