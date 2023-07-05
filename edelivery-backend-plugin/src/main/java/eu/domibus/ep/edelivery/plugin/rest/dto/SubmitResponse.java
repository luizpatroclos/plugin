package eu.domibus.ep.edelivery.plugin.rest.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubmitResponse implements Serializable {

    private static final long serialVersionUID = 4918575297106992289L;
    private List<String> messageID;


    public List<String> getMessageID() {
        if (messageID == null) {
            messageID = new ArrayList<String>();
        }
        return this.messageID;
    }



}
