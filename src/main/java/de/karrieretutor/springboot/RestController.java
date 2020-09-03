package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/app/")
public class RestController {
    Logger LOG = LoggerFactory.getLogger(RestController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    ProduktRepository produktRepository;

    @Autowired
    SimpleController simpleController;


    @GetMapping("/warenkorb")
    public Warenkorb ladeWarenkorb() {
        return simpleController.warenkorb;
/*
        Warenkorb testDaten = new Warenkorb();
        testDaten.getProdukte().add(new Produkt("Segyway", "Taiwan", Kategorie.KAT1, Unterkategorie.SUBKAT1, 1L, 499.0));
        testDaten.getProdukte().add(new Produkt("Scooter", "HongKong", Kategorie.KAT1, Unterkategorie.SUBKAT2, 2L, 299.0));
        testDaten.getProdukte().add(new Produkt("Hoverboard", "Korea", Kategorie.KAT1, Unterkategorie.SUBKAT3, 3L, 199.00));
        return testDaten;
*/
    }

    @GetMapping("/produkte")
    public List<Produkt> ladeProdukte() {
        List<Produkt> produkte = new ArrayList<>();
        produktRepository.findAll().forEach(produkte::add);
        return produkte;
    }

}
