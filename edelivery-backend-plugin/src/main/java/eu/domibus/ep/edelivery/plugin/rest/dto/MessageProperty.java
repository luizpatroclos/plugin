
package eu.domibus.ep.edelivery.plugin.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageProperty implements Serializable
{

    private static final long serialVersionUID = -5885765422262791322L;

    public void setProperty(List<Property> property) {
        this.property = property;
    }

    private List<Property> property;

    public List<Property> getProperty() {

        if(property == null){
            property = new ArrayList<Property>();
        }
        return property;
    }
}
