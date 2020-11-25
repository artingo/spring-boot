package de.karrieretutor.springboot.model;

public class Produkt {
    public String name;
    public String herkunft;
    public Kategorie kategorie;
    public Unterkategorie unterkategorie;
    Double preis;
    String foto;

    public Produkt(String name, String herkunft, Kategorie kategorie, Unterkategorie unterkategorie, Double preis) {
        this.name = name;
        this.herkunft = herkunft;
        this.kategorie = kategorie;
        this.unterkategorie = unterkategorie;
        this.preis = preis;
    }

    public String getFoto() {
        switch(this.unterkategorie) {
            case SUBKAT1:
                return "images/example-work01.jpg";
            case SUBKAT2:
                return "images/example-work07.jpg";
            case SUBKAT3:
                return "images/example-work02.jpg";
        }
        return "images/example-work03.jpg";
    }
}

