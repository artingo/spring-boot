package de.karrieretutor.springboot.model;

import java.util.ArrayList;
import java.util.List;

public class Warenkorb {
    private List<Produkt> produkte = new ArrayList<>();

    public List<Produkt> getProdukte() {
        return produkte;
    }
    public void setProdukte(List<Produkt> produkte) {
        this.produkte = produkte;
    }

    public String getGesamtpreis() {
        double gesamtpreis = 0;
        for (Produkt produkt : produkte) {
            gesamtpreis += produkt.preis;
        }
        return String.format("%.2f", gesamtpreis);
    }

    public int getGesamtzahl() {
        return produkte.size();
    }

    private Produkt findProduktById(Long produktId) {
        return produkte.stream().filter(produkt -> produkt.id == produktId).findFirst().orElse(null);
/*
        for (Produkt aktuellesProdukt : produkte) {
            if (aktuellesProdukt.id == produktId) {
                return aktuellesProdukt;
            }
        }
        return null;
*/
    }

    public void produktHinzufuegen(Produkt produkt) {
        produkte.add(produkt);
    }

    public Produkt produktEntfernen(Long produktId) {
        Produkt warenkorbProdukt = findProduktById(produktId);
        if (warenkorbProdukt != null) {
            produkte.remove(warenkorbProdukt);
            return warenkorbProdukt;
        }
        return null;
    }
}
