
package eu.domibus.ep.edelivery.plugin.rest.dto;

import java.io.Serializable;


public class AgreementRef implements Serializable
{

    private static final long serialVersionUID = 6791638764801417377L;
    private String type;
    private String pmode;
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPmode() {
        return pmode;
    }

    public void setPmode(String pmode) {
        this.pmode = pmode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
