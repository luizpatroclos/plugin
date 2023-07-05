package eu.domibus.ep.edelivery.plugin.rest.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.domibus.ep.edelivery.plugin.rest.dto.*;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestConfigHelper {

    public String getNewSubmitMessageJson(SubmitMessage submitMessage) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(submitMessage);
    }

    public Object getNewSubmitMessage(int testType) throws JsonProcessingException {

        UserMessage userMessage = new UserMessage();
        Messaging messaging = new Messaging("yes",userMessage);

        // ########## User message #############
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setTimestamp(LocalDateTime.now());
        PartyInfo partyInfo = new PartyInfo();
        From from = new From();
        PartyId partyIdFrom = new PartyId();
        partyIdFrom.setType("urn:oasis:names:tc:ebcore:partyid-type:unregistered");
        partyIdFrom.setValue("domibus-red");
        from.setRole("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/initiator");
        from.setPartyId(partyIdFrom);

        To to = new To();
        PartyId partyIdTo = new PartyId();
        partyIdTo.setType("urn:oasis:names:tc:ebcore:partyid-type:unregistered");
        partyIdTo.setValue("domibus-blue");
        to.setPartyId(partyIdFrom);
        to.setRole("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/responder");
        partyInfo.setFrom(from);
        partyInfo.setTo(to);

        CollaborationInfo collaborationInfo = new CollaborationInfo();
        collaborationInfo.setAction("TC1Leg1");
        Service service = new Service();
        service.setType("bdx:noprocess");
        service.setValue("tc1");

        AgreementRef agreementRef = new AgreementRef();
        agreementRef.setType("OK");
        agreementRef.setPmode("PMODE");
        collaborationInfo.setService(service);
        collaborationInfo.setAgreementRef(agreementRef);

        MessageProperty messageProperty = new MessageProperty();
        Property propertySender = new Property();
        propertySender.setName("originalSender");
        propertySender.setValue("urn:oasis:names:tc:ebcore:partyid-type:unregistered:C1");

        Property propertyRecipient = new Property();
        propertyRecipient.setName("finalRecipient");
        propertyRecipient.setValue("urn:oasis:names:tc:ebcore:partyid-type:unregistered:C4");

        List<Property> propertyList = new ArrayList<>();
        propertyList.add(propertySender);
        propertyList.add(propertyRecipient);
        messageProperty.setProperty(propertyList);

        PayloadInfo payloadInfo = new PayloadInfo();
        PartInfo partInfo = new PartInfo();
        partInfo.setHref(testType == 0 ? "cid:message" : null);
        Property property2 = new Property();
        List<Property> propertyList2 = new ArrayList<>();
        property2.setName("MimeType");
        property2.setType("text/xml");
        property2.setValue("text/xml");

        propertyList2.add(property2);

        PartProperties partProperties = new PartProperties();
        partProperties.setProperty(propertyList2);
        partInfo.setPartProperties(partProperties);
        List<PartInfo> partInfoList = new ArrayList<>();
        partInfoList.add(partInfo);
        payloadInfo.setPartInfo(partInfoList);

        userMessage.setMpc(null);
        userMessage.setMessageInfo(messageInfo);
        userMessage.setMessageProperties(messageProperty);
        userMessage.setCollaborationInfo(collaborationInfo);
        userMessage.setPartyInfo(partyInfo);
        userMessage.setPayloadInfo(payloadInfo);
        // ########## end User Message #############

        // ########## Submit Request or Response #############
        List<Payload> payloadList = new ArrayList<Payload>();
        Payload payload = new Payload();
//          FileInputStream fis = new FileInputStream("src/test/resources/test.txt");
//          String data = IOUtils.toString(fis, "UTF-8");
        String file = "testFile.txt";
        payload.setPayloadId("cid:message");
        payload.setValue(file.getBytes());
        payload.setContentType("tex/html");
        payloadList.add(payload);
        Bodyload bodyload = null;
        SubmitRequest submitRequest = new SubmitRequest( bodyload, payloadList);
        MessageResponse submitResponse = new MessageResponse(payloadList);
        // ########## end Submit Request #############

        return testType == 0 ? new SubmitMessage(messaging, submitRequest) : new RetrieveMessage(messaging, submitResponse);
    }

    public ErrorResultArray getErrorMessage(){

        ErrorResultArray errorResultArray = new ErrorResultArray();
        ErrorResultPlugin errorResult = new ErrorResultPlugin();
        errorResult.setErrorCode(ErrorResultCode.EBMS_0005);
        errorResult.setErrorDetail("Error dispatching message to http://192.168.1.4:8080/domibus/services/msh");
        errorResult.setMessageInErrorId("4b5d5dd2-9c62-11eb-9ab4-166b3d534cb4@domibus.eu");
        errorResult.setMshRole(MshRole.SENDING);
        errorResult.setTimestamp(LocalDateTime.now());

        errorResultArray.setStatus("Error List found for {messageId}");
        errorResultArray.getItem().add(errorResult);

        return errorResultArray;
    }

    public MockMultipartFile getMultipartFile() throws IOException {

        FileInputStream fis = new FileInputStream("src/test/resources/test.txt");

        return new MockMultipartFile("file",
                "testFile", "text/plain", IOUtils.toByteArray(fis));
    }

    public MockMultipartFile getMessageJsonFile() throws IOException {

       return new MockMultipartFile("message",
                "message", MediaType.APPLICATION_JSON_VALUE,
               getNewSubmitMessageJson((SubmitMessage) getNewSubmitMessage(0)).getBytes(StandardCharsets.UTF_8));
    }

    public ListPendingMessagesResponse listMessagesResponse(){

        ListPendingMessagesResponse list = new ListPendingMessagesResponse();

        list.getMessageIds().addAll(Arrays.asList("7a68fffc-b23e-11eb-8064-16c6102c8992@domibus.eu_1",
                "defd0c68-b252-11eb-a9e2-16c6102c8992@domibus.eu_1",
                "ca75c6a6-b325-11eb-b687-16c6102c8992@domibus.eu_1",
                "f974bd16-b343-11eb-9338-16c6102c8992@domibus.eu_1",
                "17c9ac28-b345-11eb-9338-16c6102c8992@domibus.eu_1"));

        return  list;
    }

}
