package de.karrieretutor.springboot.enums;

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
}
