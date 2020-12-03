package de.karrieretutor.springboot.enums;

public enum Kategorie {
    KAT1("enum.category.1"),
    KAT2("enum.category.2"),
    KAT3("enum.category.3"),
    KAT4("enum.category.4");

    private final String messageKey;

    Kategorie(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}