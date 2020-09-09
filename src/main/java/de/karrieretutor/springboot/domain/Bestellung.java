package de.karrieretutor.springboot.domain;

import java.util.List;

public class Bestellung {
    Long id;
    Kunde kunde;
    List<Produkt> produkte;
}
