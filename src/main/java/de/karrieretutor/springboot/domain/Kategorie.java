package de.karrieretutor.springboot.domain;

public enum Kategorie {
    KAT1("enum.category.1"),
    KAT2("enum.category.2"),
    KAT3("enum.category.3"),
    KAT4("enum.category.4");

    private final String messageKey;

    Kategorie(String displayValue) {
        this.messageKey = displayValue;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
