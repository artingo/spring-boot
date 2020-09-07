package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.ProduktRepository;
import de.karrieretutor.springboot.domain.Warenkorb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
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
    }

    @GetMapping("/produkte")
    public List<Produkt> ladeProdukte() {
        if (produkte.isEmpty()) {
            produktRepository.findAll().forEach(produkte::add);
        }
        return produkte;
    }

    @GetMapping("/messages")
    public Properties ladeMessages(@RequestParam(defaultValue = "de") String lang) {
        Properties prop = new Properties();
        String fileName = "messages_" + lang.toLowerCase() + ".properties";
        InputStream input = RestController.class.getClassLoader().getResourceAsStream(fileName);
        try {
            if (input==null)
                RestController.class.getClassLoader().getResourceAsStream("messages_en.properties");
            prop.load(new InputStreamReader(input, Charset.forName("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

}
