package eu.domibus.ep.edelivery.plugin.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class ErrorResultArray {

    private String status;
    private List<ErrorResultPlugin> item;

    public List<ErrorResultPlugin> getItem() {
        if (item == null) {
            item = new ArrayList<ErrorResultPlugin>();
        }
        return this.item;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
