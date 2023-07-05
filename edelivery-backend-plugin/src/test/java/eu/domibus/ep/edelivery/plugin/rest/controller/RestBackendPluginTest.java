package eu.domibus.ep.edelivery.plugin.rest.controller;

import eu.domibus.ep.edelivery.plugin.rest.dao.InMemoryDataBaseConfig;
import eu.domibus.ep.edelivery.plugin.rest.dao.MessageLogDao;
import eu.domibus.ep.edelivery.plugin.rest.dto.ErrorResultArray;
import eu.domibus.ep.edelivery.plugin.rest.dto.ListPendingMessagesResponse;
import eu.domibus.ep.edelivery.plugin.rest.dto.RetrieveMessage;
import eu.domibus.ep.edelivery.plugin.rest.entity.MessageLogEntity;
import eu.domibus.ep.edelivery.plugin.rest.properties.RestConfigHelper;
import eu.domibus.ep.edelivery.plugin.rest.properties.RestContextSetConfig;
import eu.domibus.ep.edelivery.plugin.rest.service.JsonModelTransformer;
import eu.domibus.ep.edelivery.plugin.rest.service.PluginHandleMessageService;
import eu.domibus.ep.edelivery.plugin.rest.service.RestPluginValidationException;
import eu.domibus.ext.services.MessageExtService;
import eu.domibus.plugin.handler.MessageRetriever;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {InMemoryDataBaseConfig.class, RestContextSetConfig.class})
@ActiveProfiles("IN_MEMORY_DATABASE")
public class RestBackendPluginTest {

    private MockMvc mockMvc;

    private RestConfigHelper restConfigTest;

    @Autowired
    private RestBackendPlugin restBackendPlugin;

    @Autowired
    private RestResponseEntityExceptionHandler responseEntityExceptionHandler;

    @Autowired
    private PluginHandleMessageService pluginHandleMessageService;

    @Autowired
    private MessageExtService messageExtService;

    @Autowired
    private JsonModelTransformer jsonModelTransformer;

    @Autowired
    private MessageRetriever messageRetriever;

    @Autowired
    public MessageLogDao messageLogDao;


    private static final String MESSAGE_ID = "4b5d5dd2-9c62-11eb-9ab4-166b3d534cb4@domibus.eu";
    private static final String END_POINT = "/ext/api/v1/ep";
    private MockMultipartFile multipartFile1 = null;
    private MockMultipartFile multipartFile2 = null;
    private MockMultipartFile messageJson = null;

    @Before
    public void setUp() throws IOException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(restBackendPlugin)
                .setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
        initMocks(this);

        restConfigTest = new RestConfigHelper();
        messageJson = restConfigTest.getMessageJsonFile();
        multipartFile2 = restConfigTest.getMultipartFile();
        multipartFile1 = restConfigTest.getMultipartFile();
    }

    @Test
    public void sendMessageWhenResultWithSuccess() throws Exception {
        //given
        given(restBackendPlugin.submit(any())).willReturn(MESSAGE_ID);

        mockMvc.perform(multipart(END_POINT+"/submitmessage")
                .file(multipartFile1)
                .file(multipartFile2)
                .file(messageJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(notNullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageID[0]", is(MESSAGE_ID)));
    }

    @Test
    public void throwExceptionWhenFileIsGreaterThanAllowed() throws Exception {
        //given
        given(restBackendPlugin.submit(any())).willThrow(new RestPluginValidationException("Invalid Message","Exceed file size"));

        mockMvc.perform(multipart(END_POINT+"/submitmessage")
                .file(multipartFile1)
                .file(multipartFile2)
                .file(messageJson))
                .andExpect(status().is(406))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(notNullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]", anything()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("NOT_ACCEPTABLE")));
    }

    @Test
    public void retrieveMessageWhenExistsAndResultWhitSuccess() throws Exception {

        //given
        RetrieveMessage retrieveMessage = (RetrieveMessage) restConfigTest.getNewSubmitMessage(1);

        given(messageExtService.cleanMessageIdentifier(anyString())).willReturn(MESSAGE_ID);
        given(restBackendPlugin.downloadMessage(MESSAGE_ID, anyObject())).willReturn(retrieveMessage.getMessaging().getUserMessage());
        given(messageLogDao.findByMessageId(MESSAGE_ID)).willReturn(new MessageLogEntity());
        given(pluginHandleMessageService.fillInfoPartsForLargeFiles(anyObject())).willReturn(retrieveMessage);

        mockMvc.perform(get(END_POINT+"/retrievemessage/"+ MESSAGE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(notNullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messaging.userMessage.collaborationInfo.action", is("TC1Leg1") ))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageResponse.payload[0].payloadId", is("cid:message")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageResponse.payload[0].contentType", is("tex/html")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageResponse.payload[0].value", is("dGVzdEZpbGUudHh0")))
                .andReturn().getResponse().containsHeader("Content-Type:\"application/json\"");

    }

    @Test
    public void getStatusMessageWhenReturnWithoutError() throws Exception {

        given(messageExtService.cleanMessageIdentifier(anyString())).willReturn(MESSAGE_ID);

        mockMvc.perform(get(END_POINT+"/status/"+ MESSAGE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void getErrorMessageWhenReturnWithoutError() throws Exception {

        ErrorResultArray errorResultArray = restConfigTest.getErrorMessage();

        given(jsonModelTransformer.transformFromErrorResults(MESSAGE_ID,
                messageRetriever.getErrorsForMessage(MESSAGE_ID))).willReturn(errorResultArray);

        mockMvc.perform(get(END_POINT+"/error/"+ MESSAGE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(notNullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("Error List found for {messageId}") ))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item[0].errorCode", is("EBMS_0005") ))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item[0].messageInErrorId", is(MESSAGE_ID)))
                .andReturn().getResponse().containsHeader("Content-Type:\"application/json\"");

    }

    @Test
    public void listPendingMessageWhenReturnWithoutError() throws Exception {

        final ListPendingMessagesResponse messageID = restConfigTest.listMessagesResponse();

        given(pluginHandleMessageService.processPendingMessages(messageLogDao.findAll(eq(5)))).willReturn(messageID);

        mockMvc.perform(get(END_POINT+"/listpendingmessages/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(notNullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageIds[0]", is("7a68fffc-b23e-11eb-8064-16c6102c8992@domibus.eu_1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageIds[1]", is("defd0c68-b252-11eb-a9e2-16c6102c8992@domibus.eu_1")))
                .andReturn();

    }

}