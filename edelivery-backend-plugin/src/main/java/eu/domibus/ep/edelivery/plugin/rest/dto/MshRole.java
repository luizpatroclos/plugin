package eu.domibus.ep.edelivery.plugin.rest.dto;

public enum MshRole {

    SENDING,
    RECEIVING;

    public String value() {
        return name();
    }

    public static MshRole fromValue(String v) {
        return valueOf(v);
    }
}
