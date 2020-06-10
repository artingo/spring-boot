package de.karrieretutor.springboot.domain;

public enum Kategorie {
    WEISSWEIN("Weißwein"),
    ROTWEIN("Rotwein"),
    ROSE("Rosé"),
    SPARKLING("Sparkling");

    private final String displayValue;

    Kategorie(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
