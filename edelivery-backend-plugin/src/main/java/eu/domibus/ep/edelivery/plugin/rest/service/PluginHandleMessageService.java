package eu.domibus.ep.edelivery.plugin.rest.service;

import eu.domibus.ep.edelivery.plugin.rest.dto.ListPendingMessagesResponse;
import eu.domibus.ep.edelivery.plugin.rest.dto.RetrieveMessage;
import eu.domibus.ep.edelivery.plugin.rest.dto.SubmitMessage;
import eu.domibus.ep.edelivery.plugin.rest.dto.UserMessage;
import eu.domibus.ep.edelivery.plugin.rest.entity.MessageLogEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PluginHandleMessageService {

    void addPartInfos(SubmitMessage message, MultipartFile[] files) throws IOException;

    RetrieveMessage fillInfoPartsForLargeFiles(UserMessage userMessage) throws IOException;

    ListPendingMessagesResponse processPendingMessages( List<MessageLogEntity> pending);

}
