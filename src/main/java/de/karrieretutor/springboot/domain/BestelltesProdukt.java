package de.karrieretutor.springboot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class BestelltesProdukt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Bestellung bestellung;

    @ManyToOne
    private Produkt produkt;

    private int anzahl;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Bestellung getBestellung() {
        return bestellung;
    }
    public void setBestellung(Bestellung bestellung) {
        this.bestellung = bestellung;
    }

    public Produkt getProdukt() {
        return produkt;
    }
    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public int getAnzahl() {
        return anzahl;
    }
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    @Override
    public String toString() {
        return "BestelltesProdukt{" +
                "id=" + id +
                ", bestellung=" + bestellung +
                ", produkt=" + produkt.toString() +
                ", anzahl=" + anzahl +
                '}';
    }
}
