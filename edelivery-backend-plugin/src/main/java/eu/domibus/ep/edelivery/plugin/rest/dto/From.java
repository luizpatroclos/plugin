
package eu.domibus.ep.edelivery.plugin.rest.dto;

import java.io.Serializable;


public class From implements Serializable
{

    private static final  long serialVersionUID = -1322483712756552913L;
    private PartyId partyId;
    private String role;

    public PartyId getPartyId() {
        return partyId;
    }

    public void setPartyId(PartyId partyId) {
        this.partyId = partyId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
