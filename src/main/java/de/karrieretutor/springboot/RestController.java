package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Bestellung;
import de.karrieretutor.springboot.domain.Kunde;
import de.karrieretutor.springboot.domain.KundenRepository;
import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.service.BestellService;
import de.karrieretutor.springboot.service.ProduktService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/app/")
public class RestController {
    Logger LOG = LoggerFactory.getLogger(RestController.class);

    @Autowired
    ProduktService produktService;

    @Autowired
    KundenRepository kundenRepository;

    @Autowired
    BestellService bestellService;

    @GetMapping("/produkte")
    public List<Produkt> ladeProdukte() {
        return produktService.ladeProdukte();
    }

    @GetMapping("/messages")
    public Properties ladeMessages(@RequestParam(defaultValue = "de") String lang) {
        Properties prop = new Properties();
        String fileName = "messages_" + lang.toLowerCase() + ".properties";
        InputStream input = RestController.class.getClassLoader().getResourceAsStream(fileName);
        try {
            if (input == null)
                RestController.class.getClassLoader().getResourceAsStream("messages_en.properties");
            prop.load(new InputStreamReader(input, Charset.forName("UTF-8")));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return prop;
    }

    @PostMapping("/bestellen")
    public Long bestellen(@RequestBody Bestellung bestellung) {
        Kunde kunde = bestellung.getKunde();
        kundenRepository.save(kunde);
        bestellService.speichere(bestellung, true);
        return kunde.getId();
    }

    @GetMapping("/kunde/{id}")
    public Kunde kundeLaden(@PathVariable Long id) {
        Kunde kunde = new Kunde();
        if (id != null) {
            Optional<Kunde> kundeDB = kundenRepository.findById(id);
            if (kundeDB.isPresent()) {
                kunde = kundeDB.get();
            }
        }
        return kunde;
    }

    @GetMapping("/bestellung/{id}")
    public Bestellung bestellung(@PathVariable Long id) {
        return bestellService.lade(id);
    }

    @GetMapping("/bestellungen/{kundenId}")
    public List<Bestellung> bestellungen(@PathVariable Long kundenId) {
        return bestellService.bestellungenVonKunde(kundenId);
    }

    @GetMapping("/fotos/{id}")
    public ResponseEntity<Resource> fotos(@PathVariable Long id) throws IOException, URISyntaxException {
        byte[] bytes = new byte[0];
        Produkt produkt = produktService.getProdukt(id);
        if (produkt == null || produkt.getDatei() == null || produkt.getDatei().length==0) {
            // wenn kein Bild hochgeladen wurde, dann lade das Standard-Bild
            URL imageURL = this.getClass().getClassLoader().getResource("./static/images/no-image.png");
            bytes = Files.readAllBytes(Paths.get(imageURL.toURI()));
        } else {
            bytes = produkt.getDatei();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new ByteArrayResource(bytes));
    }

}
