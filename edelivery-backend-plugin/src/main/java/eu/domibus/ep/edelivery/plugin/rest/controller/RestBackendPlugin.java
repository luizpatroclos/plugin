package eu.domibus.ep.edelivery.plugin.rest.controller;

import eu.domibus.common.DeliverMessageEvent;
import eu.domibus.common.ErrorCode;
import eu.domibus.common.MessageSendSuccessEvent;
import eu.domibus.ep.edelivery.plugin.rest.dao.MessageLogDao;
import eu.domibus.ep.edelivery.plugin.rest.dto.*;
import eu.domibus.ep.edelivery.plugin.rest.entity.MessageLogEntity;
import eu.domibus.ep.edelivery.plugin.rest.properties.RSPluginPropertyManager;
import eu.domibus.ep.edelivery.plugin.rest.service.JsonModelTransformer;
import eu.domibus.ep.edelivery.plugin.rest.service.PluginHandleMessageService;
import eu.domibus.ep.edelivery.plugin.rest.service.RestPluginValidationException;
import eu.domibus.ext.domain.DomainDTO;
import eu.domibus.ext.exceptions.AuthenticationExtException;
import eu.domibus.ext.exceptions.MessageAcknowledgeExtException;
import eu.domibus.ext.services.AuthenticationExtService;
import eu.domibus.ext.services.DomainContextExtService;
import eu.domibus.ext.services.MessageAcknowledgeExtService;
import eu.domibus.logging.DomibusLogger;
import eu.domibus.logging.DomibusLoggerFactory;
import eu.domibus.logging.DomibusMessageCode;
import eu.domibus.messaging.MessageNotFoundException;
import eu.domibus.messaging.MessagingProcessingException;
import eu.domibus.plugin.AbstractBackendConnector;
import eu.domibus.plugin.transformer.MessageRetrievalTransformer;
import eu.domibus.plugin.transformer.MessageSubmissionTransformer;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/ext/api/v1/ep")
@Validated
public class RestBackendPlugin extends AbstractBackendConnector<Messaging, UserMessage> {

    public static final String PLUGIN_NAME = "restBackendPlugin";

    public static final String MESSAGE_SUBMISSION_FAILED = "Message submission failed";

    private static final String MESSAGE_NOT_FOUND_ID = "Message not found, id [";

    @Autowired
    private JsonModelTransformer jsonModelTransformer;

    public static final DomibusLogger LOG = DomibusLoggerFactory.getLogger(RestBackendPlugin.class);

    @Autowired
    private PluginHandleMessageService pluginHandleMessageService;

    @Autowired
    private MessageLogDao messageLogDao;

    @Autowired
    private MessageAcknowledgeExtService messageAcknowledgeExtService;

    @Autowired
    private RSPluginPropertyManager propertyManager;

    @Autowired
    private AuthenticationExtService authenticationExtService;

    @Autowired
    private DomainContextExtService domainContextExtService;

    public RestBackendPlugin() {super(PLUGIN_NAME);}

    @ApiOperation(value = "Get status message", notes = "Retrieve the status message for a specific message id",
            authorizations = @Authorization(value = "basicAuth"), tags = "status")
    @GetMapping("/status/{messageId:.+}")
    @ResponseStatus(HttpStatus.OK)
    public MessageStatusResponse getMessageStatus(@PathVariable("messageId") String messageId){

        String trimmedMessageId = messageExtService.cleanMessageIdentifier(messageId);

        return  jsonModelTransformer.transformFromMessageStatus(getStatus(trimmedMessageId));
    }

    @ApiOperation(value = "Get error message", notes = "Retrieve the error message for a specific message id",
            authorizations = @Authorization(value = "basicAuth"), tags = "error")
    @GetMapping("/error/{messageId:.+}")
    @ResponseStatus(HttpStatus.OK)
    public ErrorResultArray getMessageError(@PathVariable("messageId") String messageId){

        return jsonModelTransformer.transformFromErrorResults(messageId, getErrorsForMessage(messageId));
    }

    @ApiOperation(value = "Retrieve received message", notes = "Retrieve message receives in a specific queue by inform the message id",
            authorizations = @Authorization(value = "basicAuth"), tags = "retrievemessage")
    @GetMapping("/retrievemessage/{messageId:.+}")
    public RetrieveMessage retrieveMessage(@PathVariable("messageId") String messageId) throws RestPluginValidationException, IOException {

        UserMessage userMessage;

        String trimmedMessageId = messageExtService.cleanMessageIdentifier(messageId);

        MessageLogEntity messageLogEntity = messageLogDao.findByMessageId(trimmedMessageId);
        if(messageLogEntity == null){
            LOG.businessError(DomibusMessageCode.BUS_MSG_NOT_FOUND, trimmedMessageId);
            throw new RestPluginValidationException(MESSAGE_NOT_FOUND_ID + trimmedMessageId + "]", "No message with id [" + trimmedMessageId + "] pending for download" );
        }

        try {
            userMessage = downloadMessage(trimmedMessageId, null);
        } catch (final MessageNotFoundException mnfEx) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(MESSAGE_NOT_FOUND_ID + messageId + "]", mnfEx);
            }
            LOG.error(MESSAGE_NOT_FOUND_ID + messageId + "]");
            throw new RestPluginValidationException(MESSAGE_NOT_FOUND_ID + trimmedMessageId + "]", mnfEx);
        }

        if (userMessage == null) {
            throw new RestPluginValidationException(MESSAGE_NOT_FOUND_ID + trimmedMessageId + "]", "UserMessage not found");
        }

        // To avoid blocking errors during the Header's response validation
        if (StringUtils.isEmpty(userMessage.getCollaborationInfo().getAgreementRef().getValue())) {
            userMessage.getCollaborationInfo().setAgreementRef(null);
        }

        RetrieveMessage retrieveMessage = pluginHandleMessageService.fillInfoPartsForLargeFiles(userMessage);

        try {
            messageAcknowledgeExtService.acknowledgeMessageDelivered(trimmedMessageId, new Timestamp(System.currentTimeMillis()));
        } catch (AuthenticationExtException | MessageAcknowledgeExtException e) {
            //if an error occurs related to the message acknowledgement do not block the download message operation
            LOG.error("Error acknowledging message [" + messageId + "]", e);
        }

        // remove downloaded message from the plugin table containing the pending messages
        messageLogDao.delete(messageLogEntity);

        return retrieveMessage;
    }

    @ApiOperation(value = "Submit message", notes = "Submit message to the AP described on PMODE",
            authorizations = @Authorization(value = "basicAuth"), tags = "submitmessage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Message Example", required = false, dataType = "SubmitMessage", paramType = "body")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/submitmessage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SubmitResponse submitMessage(@RequestPart("message") SubmitMessage message, @RequestPart("files") MultipartFile[] files)  throws IOException {

        pluginHandleMessageService.addPartInfos(message, files);

        Messaging messaging = message.getMessaging();

        if (messaging.getUserMessage().getMessageInfo() == null) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setTimestamp(LocalDateTime.now());
            messaging.getUserMessage().setMessageInfo(messageInfo);
        } else {
            final String submittedMessageId = messaging.getUserMessage().getMessageInfo().getMessageId();
            if (StringUtils.isNotEmpty(submittedMessageId)) {
                //if there is a submitted messageId we trim it
                LOG.debug("Submitted messageId=[{}]", submittedMessageId);
                String trimmedMessageId = messageExtService.cleanMessageIdentifier(submittedMessageId);
                messaging.getUserMessage().getMessageInfo().setMessageId(trimmedMessageId);
            }
        }

        final String messageId;
        try {
            messageId = this.submit(messaging);
        } catch (final MessagingProcessingException mpEx) {
            throw new RestPluginValidationException(MESSAGE_SUBMISSION_FAILED,mpEx);
        }
        LOG.info("Received message from backend with messageID [{}]", messageId);
        final SubmitResponse response = new SubmitResponse();
        response.getMessageID().add(messageId);

        return response;

    }

    @ApiOperation(value = "List Pending Messages", notes = "It will list all message IDs available for downloading",
            authorizations = @Authorization(value = "basicAuth"), tags = "listpendingmessages")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/listpendingmessages")
    public ListPendingMessagesResponse listPendingMessage(){

        DomainDTO domainDTO = domainContextExtService.getCurrentDomainSafely();
        LOG.info("ListPendingMessages for domain [{}]", domainDTO);

        final int intMaxPendingMessagesRetrieveCount = propertyManager.getKnownIntegerPropertyValue(RSPluginPropertyManager.PROP_LIST_PENDING_MESSAGES_MAXCOUNT);

        LOG.debug("maxPendingMessagesRetrieveCount [{}]", intMaxPendingMessagesRetrieveCount);

        String originalUser = null;
        if (!authenticationExtService.isUnsecureLoginAllowed()) {
            originalUser = authenticationExtService.getOriginalUser();
            LOG.info("Original user is [{}]", originalUser);
        }

        List<MessageLogEntity> pending;
        if (originalUser != null) {
            pending = messageLogDao.findAllByFinalRecipient(intMaxPendingMessagesRetrieveCount, originalUser);
        } else {
            pending = messageLogDao.findAll(intMaxPendingMessagesRetrieveCount);
        }
        return pluginHandleMessageService.processPendingMessages(pending);
    }

    @Override
    public MessageSubmissionTransformer<Messaging> getMessageSubmissionTransformer() {
        return this.jsonModelTransformer;
    }

    @Override
    public MessageRetrievalTransformer<UserMessage> getMessageRetrievalTransformer() {
        return this.jsonModelTransformer;
    }

    @Override
    public void deliverMessage(DeliverMessageEvent event) {
        LOG.info("Deliver message:[{}]", event);
        LOG.info("Deliver message to recipient:[{}]", event.getFinalRecipient());

        MessageLogEntity messageLogEntity = new MessageLogEntity(event.getMessageId(), event.getFinalRecipient());
        messageLogDao.create(messageLogEntity);
    }

    @Override
    public void messageSendSuccess(final MessageSendSuccessEvent event) {
        LOG.info("Message send success [{}]", event.getMessageId());
    }

}