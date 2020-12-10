package de.karrieretutor.springboot.domain;

import de.karrieretutor.springboot.enums.Zahlungsart;
import org.springframework.validation.BindingResult;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static javax.persistence.CascadeType.ALL;


@Entity
public class Kunde {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "{validation.adresse.vorname}")
    private String vorname;

    @NotBlank(message = "{validation.adresse.nachname}")
    private String nachname;

    @NotBlank(message = "{validation.adresse.strasse}")
    private String strasse;

    @NotBlank(message = "{validation.adresse.plz}")
    private String plz;

    @NotBlank(message = "{validation.adresse.ort}")
    private String ort;

    @NotNull(message = "{validation.zahlungsart.zahlungsart}")
    private Zahlungsart zahlungsart;

    private String iban;
    private String kreditkartenNr;

    @Email
    @NotBlank(message = "{validation.zahlungsart.email}")
    private String email;

    private String sprache = Locale.GERMAN.getLanguage();

    @OneToMany(mappedBy = "kunde", cascade = ALL)
    private List<Bestellung> bestellungen = new ArrayList<>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }
    public String getNameFormatiert() {
        return vorname + " " + nachname;
    }

    public String getNachname() {
        return nachname;
    }
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getStrasse() {
        return strasse;
    }
    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getPlz() {
        return plz;
    }
    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }
    public void setOrt(String ort) {
        this.ort = ort;
    }

    public Zahlungsart getZahlungsart() {
        return zahlungsart;
    }
    public void setZahlungsart(Zahlungsart zahlungsart) {
        this.zahlungsart = zahlungsart;
    }

    public String getIban() {
        return iban;
    }
    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getKreditkartenNr() {
        return kreditkartenNr;
    }
    public void setKreditkartenNr(String kreditkartenNr) {
        this.kreditkartenNr = kreditkartenNr;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getSprache() {
        return sprache;
    }
    public void setSprache(String sprache) {
        this.sprache = sprache;
    }

    public List<Bestellung> getBestellungen() {
        return bestellungen;
    }
    public void setBestellungen(List<Bestellung> bestellungen) {
        this.bestellungen = bestellungen;
    }


    // TODO: implementieren
    @Transient
    public boolean validiereZahlungsart(BindingResult result) {
        return false;
    }

    // TODO: implementieren
    private boolean validiereIBAN() {
        return false;
    }

    // TODO: implementieren
    private boolean validiereKreditkartenNr() {
        return false;
    }

    @Override
    public String toString() {
        return "Kunde{" +
                "id=" + id +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", strasse='" + strasse + '\'' +
                ", plz='" + plz + '\'' +
                ", ort='" + ort + '\'' +
                ", zahlungsart=" + zahlungsart +
                ", iban='" + iban + '\'' +
                ", kreditkartenNr='" + kreditkartenNr + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
