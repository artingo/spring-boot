package de.karrieretutor.springboot.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Wein {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Bitte geben Sie einen Namen ein")
    private String name;

    @NotBlank(message = "Bitte geben Sie ein/e Herkunftsland/-region ein")
    private String herkunft;

//    @NotEmpty(message = "Bitte geben Sie eine Kategorie ein")
    private Kategorie kategorie;

//    @NotEmpty(message = "Bitte geben Sie eine Rebsorte ein")
    private Rebsorte rebsorte;

    String foto;

    public Wein() {
    }

    public Wein(String name, String herkunft, Kategorie kategorie, Rebsorte rebsorte) {
        this.name = name;
        this.herkunft = herkunft;
        this.kategorie = kategorie;
        this.rebsorte = rebsorte;
    }

    public long getId() {
        return id;
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

    public Rebsorte getRebsorte() {
        return rebsorte;
    }

    public void setRebsorte(Rebsorte rebsorte) {
        this.rebsorte = rebsorte;
    }

    public String getFoto() {
        if (this.rebsorte == null)
            return "images/example-work03.jpg";
        switch (this.rebsorte) {
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

