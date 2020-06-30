package de.karrieretutor.springboot.domain;

import java.util.ArrayList;
import java.util.List;

public class Warenkorb {
    private List<Produkt> produkte = new ArrayList<>();
    private double gesamtpreis = 0;
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

    public double getGesamtpreis() {
        return gesamtpreis;
    }
    public void setGesamtpreis(double gesamtpreis) {
        this.gesamtpreis = gesamtpreis;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
