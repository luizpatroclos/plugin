
package eu.domibus.ep.edelivery.plugin.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartInfo implements Serializable
{

    private static final long serialVersionUID = -9130809825465519542L;
    private String href;
    private PartProperties partProperties;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public PartProperties getPartProperties() {
        return partProperties;
    }

    public void setPartProperties(PartProperties partProperties) {
        this.partProperties = partProperties;
    }

}
