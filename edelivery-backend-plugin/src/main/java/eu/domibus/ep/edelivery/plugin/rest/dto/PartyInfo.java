
package eu.domibus.ep.edelivery.plugin.rest.dto;

import java.io.Serializable;

public class PartyInfo implements Serializable
{

    private From from;
    private To to;
    private static final  long serialVersionUID = 8273969038941647916L;

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public To getTo() {
        return to;
    }

    public void setTo(To to) {
        this.to = to;
    }

}
