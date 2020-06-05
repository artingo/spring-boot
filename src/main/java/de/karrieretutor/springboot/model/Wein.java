package de.karrieretutor.springboot.model;

public class Wein {
    public String name;
    public String herkunft;
    public Kategorie kategorie;
    public Rebsorte rebsorte;
    String foto;

    public Wein(String name, String herkunft, Kategorie kategorie, Rebsorte rebsorte) {
        this.name = name;
        this.herkunft = herkunft;
        this.kategorie = kategorie;
        this.rebsorte = rebsorte;
    }

    public String getFoto() {
        switch(this.rebsorte) {
            case RIESLING:
                return "images/example-work01.jpg";
            case ZINFANDEL:
                return "images/example-work07.jpg";
            case SILVANER:
                return "images/example-work02.jpg";
        }
        return "images/example-work03.jpg";
    }
}

