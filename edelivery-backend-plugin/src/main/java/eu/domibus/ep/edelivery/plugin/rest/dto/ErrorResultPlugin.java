package eu.domibus.ep.edelivery.plugin.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ErrorResultPlugin implements Serializable {

    private static final long serialVersionUID = 1L;
    private ErrorResultCode errorCode;
    private String errorDetail;
    private String messageInErrorId;
    private MshRole mshRole;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime notified;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime timestamp;

    public ErrorResultCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorResultCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public String getMessageInErrorId() {
        return messageInErrorId;
    }

    public void setMessageInErrorId(String messageInErrorId) {
        this.messageInErrorId = messageInErrorId;
    }

    public MshRole getMshRole() {
        return mshRole;
    }

    public void setMshRole(MshRole mshRole) {
        this.mshRole = mshRole;
    }

    public LocalDateTime getNotified() {
        return notified;
    }

    public void setNotified(LocalDateTime notified) {
        this.notified = notified;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
