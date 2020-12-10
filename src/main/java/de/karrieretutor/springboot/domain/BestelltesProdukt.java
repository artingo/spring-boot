package de.karrieretutor.springboot.domain;

import javax.persistence.*;

@Entity
public class BestelltesProdukt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Bestellung bestellung;

    private Integer anzahl = 0;
    @ManyToOne
    private Produkt produkt;


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

    public Integer getAnzahl() {
        return anzahl;
    }
    public void setAnzahl(Integer anzahl) {
        this.anzahl = anzahl;
    }

    public Produkt getProdukt() {
        return produkt;
    }
    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public void hinzufuegen() {
        this.anzahl++;
    }

    public void entfernen() {
        if (this.anzahl >= 0)
            this.anzahl--;
    }

    @Override
    public String toString() {
        return "BestelltesProdukt{" +
                "id=" + id +
                ", bestellung=" + bestellung +
                ", produkt=" + produkt.getId() +
                ", anzahl=" + anzahl +
                '}';
    }
}
