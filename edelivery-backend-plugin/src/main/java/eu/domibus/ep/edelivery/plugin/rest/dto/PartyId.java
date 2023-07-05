
package eu.domibus.ep.edelivery.plugin.rest.dto;

import java.io.Serializable;


public class PartyId implements Serializable
{

    private static final long serialVersionUID = -8998667193182466924L;
    private String type;
    private String value;


    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
