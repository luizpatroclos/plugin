
package eu.domibus.ep.edelivery.plugin.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayloadInfo implements Serializable
{

    private static final long serialVersionUID = -5806133950864379729L;
    private List<PartInfo> partInfo = null;

    public List<PartInfo> getPartInfo() {
        if (partInfo == null) {
            partInfo = new ArrayList<PartInfo>();
        }
        return this.partInfo;
    }

    public void setPartInfo(List<PartInfo> partInfo) {
        this.partInfo = partInfo;
    }

}
