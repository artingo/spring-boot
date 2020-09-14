package de.karrieretutor.springboot.enums;

public enum BestellStatus {
    OFFEN(      "enum.bestellstatus.1"),
    VERSANDT(   "enum.bestellstatus.2"),
    GELIEFERT(  "enum.bestellstatus.3"),
    VERLOREN(   "enum.bestellstatus.4");

    private final String messageKey;

    BestellStatus(String displayValue) {
        this.messageKey = displayValue;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
