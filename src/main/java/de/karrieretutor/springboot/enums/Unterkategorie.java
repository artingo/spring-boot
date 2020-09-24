package de.karrieretutor.springboot.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Unterkategorie {
    SUBKAT1("enum.subcategory.1"),
    SUBKAT2("enum.subcategory.2"),
    SUBKAT3("enum.subcategory.3"),
    SUBKAT4("enum.subcategory.4"),
    SUBKAT5("enum.subcategory.5");

    private final String messageKey;

    Unterkategorie(String messageKey) {
        this.messageKey = messageKey;
    }

    @JsonValue
    public String getMessageKey() {
        return messageKey;
    }

    public String getMessage() {
        return "";
    }
}
