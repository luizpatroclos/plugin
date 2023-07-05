package eu.domibus.ep.edelivery.plugin.rest.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListPendingMessagesResponse implements Serializable {

    private List<String> messageIds;

    public List<String> getMessageIds() {
        if(messageIds == null){
            messageIds = new ArrayList<String>();
        }
        return messageIds;
    }

}
