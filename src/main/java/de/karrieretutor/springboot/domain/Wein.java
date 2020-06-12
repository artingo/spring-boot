package de.karrieretutor.springboot.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "WEIN")
public class Wein {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Bitte geben Sie einen Namen ein")
    private String name;

    @NotBlank(message = "Bitte geben Sie ein/e Herkunftsland/-region ein")
    private String herkunft;

    @NotNull(message = "Bitte geben Sie eine Kategorie ein")
    private Kategorie kategorie;

    @NotNull(message = "Bitte geben Sie eine Rebsorte ein")
    private Rebsorte rebsorte;

    @Min(value = 5, message = "Der Preis muss größer als 5€ sein")
    private double preis = 5;

    String foto;

    public Wein() {
    }

    public Wein(String name, String herkunft, Kategorie kategorie, Rebsorte rebsorte) {
        this.name = name;
        this.herkunft = herkunft;
        this.kategorie = kategorie;
        this.rebsorte = rebsorte;
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

    public Rebsorte getRebsorte() {
        return rebsorte;
    }
    public void setRebsorte(Rebsorte rebsorte) {
        this.rebsorte = rebsorte;
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
        if (this.rebsorte == null)
            return "images/example-work05.jpg";
        switch (this.rebsorte) {
            case RIESLING:
                return "images/example-work01.jpg";
            case ZINFANDEL:
                return "images/example-work07.jpg";
            case SILVANER:
                return "images/example-work02.jpg";
            case ICH_HAB_KEINE_AHNUNG:
                return "images/example-work02.jpg";
        }
        return "images/example-work04.jpg";
    }
}

