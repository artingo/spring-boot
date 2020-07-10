package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.ProduktRepository;
import de.karrieretutor.springboot.domain.Warenkorb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
@RequestMapping(value = "/")
public class SimpleController {
    @Autowired
    ProduktRepository produktRepository;
    Warenkorb warenkorb = new Warenkorb();

    @GetMapping("/")
    public String homePage(Model model) {
        return "redirect:/index.html";
    }

    @GetMapping("/{name}.html")
    public String htmlMapping(@PathVariable(name = "name") String name, Model model) {
        model.addAttribute("titel", StringUtils.capitalize(name));
        model.addAttribute("produkte", produktRepository.findAll());
        model.addAttribute("warenkorb", this.warenkorb);
        return name;
    }

    @GetMapping("/fotos/{id}")
    public ResponseEntity<Resource> fotos(@PathVariable Long id) throws IOException, URISyntaxException {
        Produkt produkt = new Produkt();
        byte[] bytes = new byte[0];
        if (id != null) {
            Optional<Produkt> produktDB = produktRepository.findById(id);
            if (produktDB.isPresent()) {
                produkt = produktDB.get();
                bytes = produkt.getDatei();
                // wenn kein Bild hochgeladen wurde, dann lade das Standard-Bild
                if (bytes == null || bytes.length == 0) {
                    URL imageURL = this.getClass().getClassLoader().getResource("./static/images/no-image.png");
                    bytes = Files.readAllBytes(Paths.get(imageURL.toURI()));
                }
            }
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new ByteArrayResource(bytes));
    }

    @GetMapping("/warenkorb.html")
    public String ladeWarenkorb(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("titel", "Warenkorb");
        model.addAttribute("warenkorb", this.warenkorb);
        return "warenkorb";
    }

    @GetMapping("/kaufen")
    public String kaufen(@RequestParam Long id, Model model, RedirectAttributes redirect) {
        String message = "Produkt mit der ID \"" + id + "\" nicht gefunden.";
        if (id != null) {
            Optional<Produkt> produktDB = produktRepository.findById(id);
            if (produktDB.isPresent()) {
                Produkt aktuellesProdukt = produktDB.get();
                warenkorb.getProdukte().add(aktuellesProdukt);
                message = "Produkt \"" + aktuellesProdukt.getName() + "\" zum Warenkorb hinzugefügt.";
            }
        }
        redirect.addFlashAttribute("message", message);
        return "redirect:index.html";
    }

    @GetMapping("/entfernen")
    public String entfernen(@RequestParam Long id, Model model, RedirectAttributes redirect) {
        String message = "Produkt nicht gefunden.";
        if (id != null) {
            Produkt gefundenesProdukt = warenkorb.getProdukte().stream()
                    .filter(p -> id.equals(p.getId()))
                    .findFirst().get();
            if (gefundenesProdukt != null) {
                warenkorb.getProdukte().remove(gefundenesProdukt);
                message = "Produkt \"" + gefundenesProdukt.getName() + "\" vom Warenkorb entfernt.";
            } else {
                message = "Produkt mit ID \"" + id + "\" nicht im Warenkorb gefunden.";
            }
        }
        redirect.addFlashAttribute("message", message);
        model.addAttribute("titel", "Warenkorb");
        model.addAttribute("warenkorb", warenkorb);
        return "redirect:/warenkorb.html";
    }
}
