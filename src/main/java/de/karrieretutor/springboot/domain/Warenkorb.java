package de.karrieretutor.springboot.domain;

import java.util.ArrayList;
import java.util.List;

public class Warenkorb {
    private List<Produkt> produkte = new ArrayList<>();
    private Long userId;

    public Warenkorb() {}

    public Warenkorb(Long userId) {
        this.userId = userId;
    }

    public List<Produkt> getProdukte() {
        return produkte;
    }
    public void setProdukte(List<Produkt> produkte) {
        this.produkte = produkte;
    }

    public String getGesamtpreis() {
        double gesamtpreis = 0;
        for(Produkt p : produkte) {
            gesamtpreis += p.getPreis();
        }
        return String.format("%.2f", gesamtpreis);
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
