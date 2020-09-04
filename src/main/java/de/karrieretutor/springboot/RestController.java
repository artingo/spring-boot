package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.ProduktRepository;
import de.karrieretutor.springboot.domain.Warenkorb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/app/")
public class RestController {
    Logger LOG = LoggerFactory.getLogger(RestController.class);

    @Autowired
    ProduktRepository produktRepository;

    @Autowired
    SimpleController simpleController;
    List<Produkt> produkte = new ArrayList<>();


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
        if (produkte.isEmpty()) {
            produktRepository.findAll().forEach(produkte::add);
        }
        return produkte;
    }

    @GetMapping("/messages")
    public Properties ladeMessages() {
        InputStream input = RestController.class.getClassLoader().getResourceAsStream("messages_de.properties");
        Properties prop = new Properties();
        try {
            prop.load(input);
        } catch (IOException e) {
        }
        return prop;
    }

}
