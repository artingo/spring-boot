package de.karrieretutor.springboot.domain;

public enum Unterkategorie {
    SUBKAT1("Unterkategorie 1"),
    SUBKAT2("Unterkategorie 2"),
    SUBKAT3("Unterkategorie 3"),
    SUBKAT4("Unterkategorie 4"),
    KEINE_AHNUNG("Ich weiß es nicht");

    private final String displayValue;

    Unterkategorie(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
