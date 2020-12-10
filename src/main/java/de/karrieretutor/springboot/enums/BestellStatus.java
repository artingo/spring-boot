package de.karrieretutor.springboot.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BestellStatus {
    OFFEN(      "enum.bestellstatus.1"),
    VERSANDT(   "enum.bestellstatus.2"),
    GELIEFERT(  "enum.bestellstatus.3"),
    VERLOREN(   "enum.bestellstatus.4"),
    RETOURE(    "enum.bestellstatus.5");

    private final String messageKey;

    BestellStatus(String displayValue) {
        this.messageKey = displayValue;
    }

    @JsonValue
    public String getMessageKey() {
        return messageKey;
    }
}
