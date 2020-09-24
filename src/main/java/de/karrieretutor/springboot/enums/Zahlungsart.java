package de.karrieretutor.springboot.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Zahlungsart {
    EINZUG("enum.payment.1"),
    KREDITKARTE("enum.payment.2"),
    PAYPAL("enum.payment.2");

    private final String messageKey;

    Zahlungsart(String displayValue) {
        this.messageKey = displayValue;
    }

    public String getMessageKey() {
        return messageKey;
    }

    @JsonValue
    public int getValue() {
        return this.ordinal();
    }

}
