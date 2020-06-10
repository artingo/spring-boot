package de.karrieretutor.springboot.domain;

public enum Rebsorte {
    RIESLING("Riesling"),
    BURGUNDER("Burgunder"),
    ZINFANDEL("Zinfandel"),
    SILVANER("Silvaner"),
    ICH_HAB_KEINE_AHNUNG("Ich weiß es nicht");

    private final String displayValue;

    Rebsorte(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
