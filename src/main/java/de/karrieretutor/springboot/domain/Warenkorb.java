package de.karrieretutor.springboot.domain;

import java.util.ArrayList;
import java.util.List;

public class Warenkorb {
    private List<BestelltesProdukt> produkte = new ArrayList<>();
    private Long userId;

    public Warenkorb() {}

    public Warenkorb(Long userId) {
        this.userId = userId;
    }

    public List<BestelltesProdukt> getProdukte() {
        return produkte;
    }
    public void setProdukte(List<BestelltesProdukt> produkte) {
        this.produkte = produkte;
    }

    public String getGesamtpreis() {
        double gesamtpreis = 0;
        for(BestelltesProdukt p : produkte) {
            Produkt produkt = p.getProdukt();
            gesamtpreis += produkt.getPreis() * p.getAnzahl();
        }
        return String.format("%.2f", gesamtpreis);
    }

    public int getGesamtzahl() {
        int gesamtzahl = 0;
        for(BestelltesProdukt p : produkte) {
            gesamtzahl += p.getAnzahl();
        }
        return gesamtzahl;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private BestelltesProdukt findProduktById(Long produktId) {
        for (int i = 0; i < produkte.size(); i++) {
            BestelltesProdukt aktuellesProdukt = produkte.get(i);
            if (aktuellesProdukt.getProdukt().getId() == produktId) {
                return aktuellesProdukt;
            }
        }
        return null;
    }

    public void produktHinzufuegen(Produkt produkt) {
        BestelltesProdukt warenkorbProdukt = findProduktById(produkt.getId());
        if (warenkorbProdukt == null) {
            warenkorbProdukt = new BestelltesProdukt();
            warenkorbProdukt.setProdukt(produkt);
            produkte.add(warenkorbProdukt);
        }
        warenkorbProdukt.hinzufuegen();
    }

    public Produkt produktEntfernen(Long produktId) {
        BestelltesProdukt warenkorbProdukt = findProduktById(produktId);
        if (warenkorbProdukt != null) {
            warenkorbProdukt.entfernen();
            if (warenkorbProdukt.getAnzahl() == 0) {
                produkte.remove(warenkorbProdukt);
            }
            return warenkorbProdukt.getProdukt();
        }
        return null;
    }
}
