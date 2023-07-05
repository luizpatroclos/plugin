package eu.domibus.ep.edelivery.plugin.rest.dto;

public enum ErrorResultCode {

    EBMS_0001,
    EBMS_0002,
    EBMS_0003,
    EBMS_0004,
    EBMS_0005,
    EBMS_0006,
    EBMS_0007,
    EBMS_0008,
    EBMS_0009,
    EBMS_0010,
    EBMS_0011,
    EBMS_0101,
    EBMS_0102,
    EBMS_0103,
    EBMS_0201,
    EBMS_0202,
    EBMS_0301,
    EBMS_0302,
    EBMS_0303,
    EBMS_0020,
    EBMS_0021,
    EBMS_0022,
    EBMS_0023,
    EBMS_0030,
    EBMS_0031,
    EBMS_0040,
    EBMS_0041,
    EBMS_0042,
    EBMS_0043,
    EBMS_0044,
    EBMS_0045,
    EBMS_0046,
    EBMS_0047,
    EBMS_0048,
    EBMS_0049,
    EBMS_0050,
    EBMS_0051,
    EBMS_0052,
    EBMS_0053,
    EBMS_0054,
    EBMS_0055,
    EBMS_0060,
    EBMS_0065;

    public String value() {
        return name();
    }

    public static ErrorResultCode fromValue(String v) {
        return valueOf(v);
    }
}
