
package eu.domibus.ep.edelivery.plugin.rest.dto;

import java.io.Serializable;


public class Service implements Serializable
{

    private static final long serialVersionUID = -862721113830111063L;
    private String value;
    private String type;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
