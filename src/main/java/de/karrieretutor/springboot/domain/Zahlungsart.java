package de.karrieretutor.springboot.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Zahlungsart {
    EINZUG("enum.payment.1"),
    KREDITKARTE("enum.payment.2"),
    PAYPAL("enum.payment.2");

    private final String messageKey;

    Zahlungsart(String displayValue) {
        this.messageKey = displayValue;
    }

    @JsonValue
    public String getMessageKey() {
        return messageKey;
    }

}
