package eu.domibus.ep.edelivery.plugin.rest.service;

import eu.domibus.common.ErrorCode;


public class RestPluginValidationException extends RuntimeException {

    private String messageId;

    public RestPluginValidationException(String s) {
        super(s);
    }

    public RestPluginValidationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RestPluginValidationException(String s, ErrorCode errorCode, String s1) {
    }

    public RestPluginValidationException(String s, String messageId) {
        super(s);
        this.setMessageId(messageId);
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
