package eu.domibus.ep.edelivery.plugin.rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.domibus.ep.edelivery.plugin.rest.dto.*;
import eu.domibus.ep.edelivery.plugin.rest.entity.MessageLogEntity;
import eu.domibus.ep.edelivery.plugin.rest.properties.RSPluginPropertyManager;
import eu.domibus.logging.DomibusLogger;
import eu.domibus.logging.DomibusLoggerFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PluginHandleMessageServiceImpl implements PluginHandleMessageService {

    private static final DomibusLogger LOG = DomibusLoggerFactory.getLogger(PluginHandleMessageServiceImpl.class);

    private static final String MIME_TYPE = "MimeType";
    private static final String INVALID_REQUEST = "Invalid request";
    private static final String CID_ATTACHMENT = "cid:attachment";
    private int countFile = 0;

    @Autowired
    private RSPluginPropertyManager rsPluginPropertyManager;

    @Override
    public void addPartInfos(SubmitMessage message, MultipartFile[] files) throws IOException {

        Messaging messaging = message.getMessaging();
        SubmitRequest submitRequest = message.getSubmitRequest();

        int filesLimit = rsPluginPropertyManager.getKnownIntegerPropertyValue("rsplugin.files.message.max");

        if (getPayloadInfo(messaging) == null) {
            return;
        }

        validateSubmitRequest(submitRequest);

        List<ExtendedPartInfo> partInfosToAdd = new ArrayList<>();
        List<PartInfo> partInfoList = getPartInfo(messaging);

        for (Iterator<PartInfo> i = partInfoList.iterator(); i.hasNext(); ) {
            ExtendedPartInfo extendedPartInfo = new ExtendedPartInfo(i.next());
            partInfosToAdd.add(extendedPartInfo);
            i.remove();

            final String href = extendedPartInfo.getHref();
            LOG.debug("Looking for payload: {}", href);

            for (final Payload payload : submitRequest.getPayload()) {
                LOG.debug("comparing with payload id: " + payload.getPayloadId());
                if (StringUtils.equalsIgnoreCase(payload.getPayloadId(), href)) {
                    this.copyPartProperties(payload.getContentType(), extendedPartInfo);
                    extendedPartInfo.setInBody(false);
                    LOG.debug("sendMessage - payload Content Type: " + payload.getContentType());
                    extendedPartInfo.setPayloadDatahandler(new DataHandler(new ByteArrayDataSource(payload.getValue(), null)));
                    break;
                }
            }
        }

        if(files.length > 0) {
            if (files.length <= filesLimit) {

            Arrays.stream(files).forEach(f -> {
                if (!f.getOriginalFilename().isEmpty()) {

                    PartInfo partInfoEx = getNewPartInfo(countFile);
                    ExtendedPartInfo extendedPartInfo = new ExtendedPartInfo(partInfoEx);
                    partInfosToAdd.add(extendedPartInfo);
                    extendedPartInfo.setInBody(false);
                    countFile++;
                    try {
                        extendedPartInfo.setPayloadDatahandler(addDataHandler(f));
                    } catch (IOException e) {
                        throw new RestPluginValidationException(INVALID_REQUEST + " - Error while convert payload: ", e);
                    }
                }
            });
            partInfoList.addAll(partInfosToAdd);
            PayloadInfo payloadInfo = new PayloadInfo();
            payloadInfo.setPartInfo(partInfoList);
            messaging.getUserMessage().setPayloadInfo(payloadInfo);
            this.countFile=0;
           }else{
                throw new RestPluginValidationException(INVALID_REQUEST+" - The maximum files allowed per message is :" + filesLimit);
            }

        }
    }
    private PartInfo getNewPartInfo(int numFile) {

        PartInfo partInfoEx = new PartInfo();
        PartProperties partPropertiesEx = new PartProperties();

        List<Property> propertyListEx = new ArrayList<>();
        Property propertyEx = new Property();
        propertyEx.setName(MIME_TYPE);
        propertyEx.setValue(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        propertyEx.setType(null);
        propertyListEx.add(propertyEx);

        partPropertiesEx.setProperty(propertyListEx);
        partInfoEx.setPartProperties(partPropertiesEx);
        partInfoEx.setHref(numFile == 0 ?CID_ATTACHMENT:CID_ATTACHMENT+numFile);
        return partInfoEx;
    }

    private PayloadInfo getPayloadInfo(Messaging messaging) {
        if (messaging.getUserMessage() == null) {
            return null;
        }
        return messaging.getUserMessage().getPayloadInfo();
    }

    protected void validateSubmitRequest(SubmitRequest submitRequest){
        for (final Payload payload : submitRequest.getPayload()) {
            if (StringUtils.isBlank(payload.getPayloadId())) {
                throw new RestPluginValidationException(INVALID_REQUEST+" - Attribute 'payloadId' of the 'payload' element must not be empty");
            }
        }
    }

    private List<PartInfo> getPartInfo(Messaging messaging) {
        PayloadInfo payloadInfo = getPayloadInfo(messaging);
        if (payloadInfo == null) {
            return new ArrayList<>();
        }
        return payloadInfo.getPartInfo();
    }

    private void copyPartProperties(final String payloadContentType, final ExtendedPartInfo partInfo) {
        final PartProperties partProperties = new PartProperties();
        Property prop;

        // add all partproperties WEBSERVICE_OF the backend message
        if (partInfo.getPartProperties() != null) {
            for (final Property property : partInfo.getPartProperties().getProperty()) {
                prop = new Property();

                prop.setName(property.getName() != null ? property.getName(): MIME_TYPE);
                prop.setValue(property.getValue());
                partProperties.getProperty().add(prop);
            }
        }

        boolean mimeTypePropFound = false;
        for (final Property property : partProperties.getProperty()) {
            if (MIME_TYPE.equals(property.getName())) {
                mimeTypePropFound = true;
                break;
            }
        }
        // in case there was no property with name {@value Property.MIME_TYPE} and xmime:contentType attribute was set noinspection SuspiciousMethodCalls
        if (!mimeTypePropFound && payloadContentType != null) {
            prop = new Property();
            prop.setName(MIME_TYPE);
            prop.setValue(payloadContentType);
            partProperties.getProperty().add(prop);
        }
        partInfo.setPartProperties(partProperties);
    }

    public DataHandler addDataHandler(MultipartFile file) throws IOException {
        return new DataHandler(new ByteArrayDataSource(file.getBytes(), null));
    }


    @Override
    public RetrieveMessage fillInfoPartsForLargeFiles(UserMessage userMessage) throws IOException {

        Messaging messaging = new Messaging();
        messaging.setUserMessage(userMessage);

        if (getPayloadInfo(messaging) == null || CollectionUtils.isEmpty(getPartInfo(messaging))) {
            LOG.info("No payload found for message [{}]", messaging.getUserMessage().getMessageInfo().getMessageId());
            throw new RestPluginValidationException("No payload found for message [{}]",messaging.getUserMessage().getMessageInfo().getMessageId());
        }

        List<Payload> payloadList = new ArrayList<>();
        Payload payload = null;

        for (final PartInfo partInfo : getPartInfo(messaging)) {
            ExtendedPartInfo extPartInfo = (ExtendedPartInfo) partInfo;
            payload = new Payload();
            payload.setPayloadId(extPartInfo.getHref());
            if (extPartInfo.getPayloadDatahandler() != null){
                payload.setValue(getBytes(extPartInfo.getPayloadDatahandler()));
            }
            if (extPartInfo.getPartProperties() != null) {
                for (final Property property : extPartInfo.getPartProperties().getProperty()) {
                    String propertyValue = trim(property.getValue());
                    payload.setContentType(propertyValue);
                }
            }
            payloadList.add(payload);
        }
        return new RetrieveMessage(messaging, new MessageResponse(payloadList));
    }

    @Override
    public ListPendingMessagesResponse processPendingMessages(List<MessageLogEntity> pending) {
        final ListPendingMessagesResponse messageIds = new ListPendingMessagesResponse();
        final Collection<String> ids = pending.stream()
                .map(MessageLogEntity::getMessageId).collect(Collectors.toList());
        messageIds.getMessageIds().addAll(ids);

        return messageIds;
    }
    protected String trim(String messageId) {
        return StringUtils.stripToEmpty(StringUtils.trimToEmpty(messageId));
    }

    private byte[] getBytes(DataHandler dataHandler) throws IOException {
        final InputStream in = dataHandler.getInputStream();
        return org.apache.commons.io.IOUtils.toByteArray(in);
    }

}
