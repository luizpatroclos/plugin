package eu.domibus.ep.edelivery.plugin.rest.dto;

public enum MessageStatus {
    READY_TO_SEND,
    READY_TO_PULL,
    BEING_PULLED,
    SEND_ENQUEUED,
    SEND_IN_PROGRESS,
    WAITING_FOR_RECEIPT,
    ACKNOWLEDGED,
    ACKNOWLEDGED_WITH_WARNING,
    SEND_ATTEMPT_FAILED,
    SEND_FAILURE,
    NOT_FOUND,
    WAITING_FOR_RETRY,
    RECEIVED,
    RECEIVED_WITH_WARNINGS,
    DELETED,
    DOWNLOADED;

    public String value() {
        return name();
    }

    public static MessageStatus fromValue(String v) {
        return valueOf(v);
    }

}
