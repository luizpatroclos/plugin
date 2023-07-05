
package eu.domibus.ep.edelivery.plugin.rest.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PartProperties implements Serializable
{

    private static final long serialVersionUID = 8343445277214759706L;
    private List<Property> property;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Property> getProperty() {
        if(property == null){
            property = new ArrayList<Property>();
        }
        return this.property;
    }

    public void setProperty(List<Property> property) {
        this.property = property;
    }
}
