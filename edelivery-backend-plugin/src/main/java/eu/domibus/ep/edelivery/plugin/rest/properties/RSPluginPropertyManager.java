package eu.domibus.ep.edelivery.plugin.rest.properties;

import eu.domibus.ext.domain.DomibusPropertyMetadataDTO;
import eu.domibus.ext.domain.DomibusPropertyMetadataDTO.Type;
import eu.domibus.ext.domain.DomibusPropertyMetadataDTO.Usage;
import eu.domibus.ext.services.DomibusPropertyExtServiceDelegateAbstract;
import eu.domibus.ext.services.DomibusPropertyManagerExt;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class RSPluginPropertyManager extends DomibusPropertyExtServiceDelegateAbstract
        implements DomibusPropertyManagerExt {

    public static final String SCHEMA_VALIDATION_ENABLED_PROPERTY = "rsplugin.schema.validation.enabled";
    public static final String MTOM_ENABLED_PROPERTY = "rsplugin.mtom.enabled";
    public static final String PROP_LIST_PENDING_MESSAGES_MAXCOUNT = "rsplugin.messages.pending.list.max";
    public static final String MESSAGE_NOTIFICATIONS = "rsplugin.messages.notifications";
    public static final String LIST_SEND_FILES_MAXCOUNT = "rsplugin.files.message.max";
    public static final String FILES_MAXFILESIZE = "rsplugin.file.maxFileSize";
    public static final String FILES_MAXREQUESTSIZE = "rsplugin.file.maxRequestSize";
    public static final String RS_PLUGIN = "RS_PLUGIN";


    private Map<String, DomibusPropertyMetadataDTO> knownProperties;

    public RSPluginPropertyManager() {
        List<DomibusPropertyMetadataDTO> allProperties = Arrays.asList(
                new DomibusPropertyMetadataDTO(SCHEMA_VALIDATION_ENABLED_PROPERTY, Type.BOOLEAN, RS_PLUGIN, Usage.GLOBAL),
                new DomibusPropertyMetadataDTO(MTOM_ENABLED_PROPERTY, Type.BOOLEAN, RS_PLUGIN, Usage.GLOBAL),
                new DomibusPropertyMetadataDTO(PROP_LIST_PENDING_MESSAGES_MAXCOUNT, Type.NUMERIC, RS_PLUGIN, Usage.GLOBAL),
                new DomibusPropertyMetadataDTO(MESSAGE_NOTIFICATIONS, Type.COMMA_SEPARATED_LIST, RS_PLUGIN, Usage.GLOBAL),
                new DomibusPropertyMetadataDTO(LIST_SEND_FILES_MAXCOUNT, Type.NUMERIC, RS_PLUGIN, Usage.GLOBAL),
                new DomibusPropertyMetadataDTO(FILES_MAXFILESIZE, Type.STRING, RS_PLUGIN, Usage.GLOBAL),
                new DomibusPropertyMetadataDTO(FILES_MAXREQUESTSIZE, Type.STRING, RS_PLUGIN, Usage.GLOBAL)
        );
        knownProperties = allProperties.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
    }

    @Override
    public Map<String, DomibusPropertyMetadataDTO> getKnownProperties() {
        return knownProperties;
    }
}