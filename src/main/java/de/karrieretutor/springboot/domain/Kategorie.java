package de.karrieretutor.springboot.domain;

public enum Kategorie {
    KAT1("Kategorie 1"),
    KAT2("Kategorie 2"),
    KAT3("Kategorie 3"),
    KAT4("Kategorie 4");

    private final String displayValue;

    Kategorie(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
