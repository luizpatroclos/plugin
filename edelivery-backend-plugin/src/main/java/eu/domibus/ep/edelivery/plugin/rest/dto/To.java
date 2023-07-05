
package eu.domibus.ep.edelivery.plugin.rest.dto;

import java.io.Serializable;

public class To implements Serializable
{

    private static final long serialVersionUID = -6846413002374519138L;
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
