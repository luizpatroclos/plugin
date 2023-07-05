package eu.domibus.ep.edelivery.plugin.rest.service;


import eu.domibus.common.ErrorResult;
import eu.domibus.ep.edelivery.plugin.rest.dto.*;
import eu.domibus.ext.services.FileUtilExtService;
import eu.domibus.logging.DomibusLogger;
import eu.domibus.logging.DomibusLoggerFactory;
import eu.domibus.messaging.MessageConstants;
import eu.domibus.plugin.Submission;
import eu.domibus.plugin.transformer.MessageRetrievalTransformer;
import eu.domibus.plugin.transformer.MessageSubmissionTransformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Converter class for Submission <-> UserMessage objects.
 * Adapted from Ws default plugin
 *
 */
@Component
public class JsonModelTransformer implements MessageSubmissionTransformer<Messaging>, MessageRetrievalTransformer<UserMessage> {

    private static final DomibusLogger LOG = DomibusLoggerFactory.getLogger(JsonModelTransformer.class);

    @Autowired
    FileUtilExtService fileUtilExtService;


    @Override
    public UserMessage transformFromSubmission(final Submission submission, final UserMessage target) {
        return transformFromSubmission(submission);
    }


    public UserMessage transformFromSubmission(final Submission submission) {
        final UserMessage result = new UserMessage();
        result.setMpc(submission.getMpc());
        this.generateCollaborationInfo(submission, result);
        this.generateMessageInfo(submission, result);
        this.generatePartyInfo(submission, result);
        this.generatePayload(submission, result);
        this.generateMessageProperties(submission, result);
        return result;
    }

    private void generateMessageProperties(final Submission submission, final UserMessage result) {

        final MessageProperty messageProperties = new MessageProperty();

        for (Submission.TypedProperty propertyEntry : submission.getMessageProperties()) {
            final Property prop = new Property();
            prop.setName(propertyEntry.getKey());
            prop.setValue(propertyEntry.getValue());
            prop.setType(propertyEntry.getType());
            messageProperties.getProperty().add(prop);
        }

        result.setMessageProperties(messageProperties);
    }

    private void generateCollaborationInfo(final Submission submission, final UserMessage result) {
        final CollaborationInfo collaborationInfo = new CollaborationInfo();
        collaborationInfo.setConversationId(submission.getConversationId());
        collaborationInfo.setAction(submission.getAction());
        final AgreementRef agreementRef = new AgreementRef();
        agreementRef.setValue(submission.getAgreementRef());
        agreementRef.setType(submission.getAgreementRefType());
        collaborationInfo.setAgreementRef(agreementRef);
        final Service service = new Service();
        service.setValue(submission.getService());
        service.setType(submission.getServiceType());
        collaborationInfo.setService(service);
        result.setCollaborationInfo(collaborationInfo);
    }

    private void generateMessageInfo(final Submission submission, final UserMessage result) {
        final MessageInfo messageInfo = new MessageInfo();
        messageInfo.setMessageId(submission.getMessageId());
        LOG.debug("MESSAGE ID " + messageInfo.getMessageId());
        messageInfo.setTimestamp(LocalDateTime.now());
        LOG.debug("TIMESTAMP " + messageInfo.getTimestamp());
        messageInfo.setRefToMessageId(submission.getRefToMessageId());
        result.setMessageInfo(messageInfo);
    }

    private void generatePartyInfo(final Submission submission, final UserMessage result) {
        final PartyInfo partyInfo = new PartyInfo();
        final From from = new From();
        from.setRole(submission.getFromRole());
        for (final Submission.Party party : submission.getFromParties()) {
            final PartyId partyId = new PartyId();
            partyId.setValue(party.getPartyId());
            partyId.setType(party.getPartyIdType());
            from.setPartyId(partyId);
        }
        partyInfo.setFrom(from);

        final To to = new To();
        to.setRole(submission.getToRole());
        for (final Submission.Party party : submission.getToParties()) {
            final PartyId partyId = new PartyId();
            partyId.setValue(party.getPartyId());
            partyId.setType(party.getPartyIdType());
            to.setPartyId(partyId);
        }
        partyInfo.setTo(to);

        result.setPartyInfo(partyInfo);
    }


    private void generatePayload(final Submission submission, final UserMessage result) {

        final PayloadInfo payloadInfo = new PayloadInfo();

        for (final Submission.Payload payload : submission.getPayloads()) {
            final ExtendedPartInfo partInfo = new ExtendedPartInfo();
            partInfo.setInBody(payload.isInBody());
            partInfo.setPayloadDatahandler(payload.getPayloadDatahandler());
            partInfo.setHref(payload.getContentId());
            final PartProperties partProperties = new PartProperties();
            for (final Submission.TypedProperty entry : payload.getPayloadProperties()) {
                final Property property = new Property();
                property.setName(entry.getKey());
                property.setValue(entry.getValue());
                property.setType(entry.getType());
                partProperties.getProperty().add(property);
            }
            partInfo.setPartProperties(partProperties);
            payloadInfo.getPartInfo().add(partInfo);
            result.setPayloadInfo(payloadInfo);
        }
    }


    @Override
    public Submission transformToSubmission(final Messaging messageData) {
        return transformFromMessaging(messageData.getUserMessage());
    }



    public Submission transformFromMessaging(final UserMessage messaging) {
        LOG.debug("Entered method: transformFromMessaging(final UserMessage messaging)");
        final Submission result = new Submission();
        result.setMpc(messaging.getMpc());
        populateMessageInfo(result, messaging);
        populatePartyInfo(result, messaging);
        populateCollaborationInfo(result, messaging);
        populateMessageProperties(result, messaging);
        populatePayloadInfo(result, messaging);
        return result;
    }

    private void populateMessageInfo(Submission result, UserMessage messaging) {
        final MessageInfo messageInfo = messaging.getMessageInfo();
        if (result == null || messageInfo == null) {
            return;
        }
        LOG.debug("Populating MessageInfo");
        result.setMessageId(messageInfo.getMessageId());
        result.setRefToMessageId(messageInfo.getRefToMessageId());
    }

    private void populatePartyInfo(Submission result, UserMessage messaging) {
        final PartyInfo partyInfo = messaging.getPartyInfo();
        if (result == null || partyInfo == null) {
            return;
        }
        LOG.debug("Populating PartyInfo");
        if (partyInfo.getFrom() != null) {
            PartyId partyId = partyInfo.getFrom().getPartyId();
            if (partyId != null) {
                result.addFromParty(partyId.getValue(), partyId.getType());
            }
            result.setFromRole(partyInfo.getFrom().getRole());
        }
        if (partyInfo.getTo() != null) {
            PartyId partyId = partyInfo.getTo().getPartyId();
            if (partyId != null) {
                result.addToParty(partyId.getValue(), partyId.getType());
            }
            result.setToRole(partyInfo.getTo().getRole());
        }
    }

    private void populateCollaborationInfo(Submission result, UserMessage messaging) {
        final CollaborationInfo collaborationInfo = messaging.getCollaborationInfo();
        if (result == null || collaborationInfo == null) {
            return;
        }
        LOG.debug("Populating CollaborationInfo");
        result.setAction(collaborationInfo.getAction());
        if (collaborationInfo.getService() != null) {
            result.setService(collaborationInfo.getService().getValue());
            result.setServiceType(collaborationInfo.getService().getType());
        }
        if (collaborationInfo.getAgreementRef() != null) {
            result.setAgreementRef(collaborationInfo.getAgreementRef().getValue());
            result.setAgreementRefType(collaborationInfo.getAgreementRef().getType());
        }
        result.setConversationId(collaborationInfo.getConversationId());
    }

    private void populateMessageProperties(Submission result, UserMessage messaging) {
        if (result == null || messaging.getMessageProperties() == null) {
            return;
        }
        LOG.debug("Populating MessageProperties");
        for (final Property property : messaging.getMessageProperties().getProperty()) {
            result.addMessageProperty(property.getName(), property.getValue(), property.getType());
        }
    }

    private void populatePayloadInfo(Submission result, UserMessage messaging) {
        final PayloadInfo payloadInfo = messaging.getPayloadInfo();
        if (result == null || payloadInfo == null) {
            return;
        }
        LOG.debug("Populating PayloadInfo");
        for (final PartInfo partInfo : payloadInfo.getPartInfo()) {
            ExtendedPartInfo extPartInfo = (ExtendedPartInfo) partInfo;
            final Collection<Submission.TypedProperty> properties = new ArrayList<>();
            if (extPartInfo.getPartProperties() != null) {
                for (final Property property : extPartInfo.getPartProperties().getProperty()) {
                    String propertyName = trim(property.getName());
                    String propertyValue = trim(property.getValue());
                    if (StringUtils.equals(propertyName, MessageConstants.PAYLOAD_PROPERTY_FILE_NAME)) {
                        LOG.debug("{} property found=[{}]", propertyName, propertyValue);
                        propertyValue = fileUtilExtService.sanitizeFileName(propertyValue);
                    }
                    properties.add(new Submission.TypedProperty(propertyName, propertyValue, trim(property.getType())));
                }
            }
            result.addPayload(extPartInfo.getHref(), extPartInfo.getPayloadDatahandler(), properties, extPartInfo.isInBody(), null, null);
        }
    }

    public MessageStatusResponse transformFromMessageStatus(eu.domibus.common.MessageStatus messageStatus) {

        MessageStatusResponse messageStatusResponse = new MessageStatusResponse();
        messageStatusResponse.setStatus(MessageStatus.fromValue(messageStatus.name()));

        return messageStatusResponse;
    }


        public ErrorResultArray transformFromErrorResults(String messageId, List<? extends ErrorResult> errors) {
            ErrorResultArray errorList = new ErrorResultArray();

            if(!errors.isEmpty()) {
                errorList.setStatus("Error List found for {messageId}: "+messageId);

                for (ErrorResult errorResult : errors) {
                    ErrorResultPlugin errorResultImpl = new ErrorResultPlugin();
                    errorResultImpl.setErrorCode(ErrorResultCode.fromValue(errorResult.getErrorCode().name()));
                    errorResultImpl.setErrorDetail(errorResult.getErrorDetail());
                    errorResultImpl.setMshRole(MshRole.fromValue(errorResult.getMshRole().name()));
                    errorResultImpl.setMessageInErrorId(errorResult.getMessageInErrorId());
                    LocalDateTime dateTime = LocalDateTime.now();

                    if (errorResult.getNotified() != null) {
                        dateTime = errorResult.getNotified().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    }
                    errorResultImpl.setNotified(dateTime);
                    if (errorResult.getTimestamp() != null) {
                        dateTime = errorResult.getTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    }
                    errorResultImpl.setTimestamp(dateTime);
                    errorList.getItem().add(errorResultImpl);
                }
            }else{
                errorList.setStatus("No Error List for {messageId}: "+messageId);
            }

        return errorList;
    }

}
