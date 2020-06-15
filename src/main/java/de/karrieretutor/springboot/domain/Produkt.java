package de.karrieretutor.springboot.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Produkt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Bitte geben Sie einen Namen ein")
    private String name;

    @NotBlank(message = "Bitte geben Sie ein/e Herkunftsland/-region ein")
    private String herkunft;

    @NotNull(message = "Bitte geben Sie eine Kategorie ein")
    private Kategorie kategorie;

    @NotNull(message = "Bitte geben Sie eine Unterkategorie ein")
    private Unterkategorie unterkategorie;

    @Min(value = 5, message = "Der Preis muss größer als 5€ sein")
    private double preis = 5;

    String foto;

    public Produkt() {
    }

    public Produkt(String name, String herkunft, Kategorie kategorie, Unterkategorie unterkategorie) {
        this.name = name;
        this.herkunft = herkunft;
        this.kategorie = kategorie;
        this.unterkategorie = unterkategorie;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getHerkunft() {
        return herkunft;
    }
    public void setHerkunft(String herkunft) {
        this.herkunft = herkunft;
    }

    public Kategorie getKategorie() {
        return kategorie;
    }
    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    public Unterkategorie getUnterkategorie() {
        return unterkategorie;
    }
    public void setUnterkategorie(Unterkategorie unterkategorie) {
        this.unterkategorie = unterkategorie;
    }

    public double getPreis() {
        return preis;
    }
    public void setPreis(double preis) {
        this.preis = preis;
    }
    public String getPreisFormatiert() {
        return String.format("%.2f", this.preis);
    }

    public String getFoto() {
        if (this.unterkategorie == null)
            return "images/example-work07.jpg";
        switch (this.unterkategorie) {
            case SUBKAT1:
                return "images/example-work01.jpg";
            case SUBKAT2:
                return "images/example-work02.jpg";
            case SUBKAT3:
                return "images/example-work03.jpg";
            case SUBKAT4:
                return "images/example-work04.jpg";
            case KEINE_AHNUNG:
                return "images/example-work05.jpg";
            default:
               return "images/example-work06.jpg";
        }
    }
}

