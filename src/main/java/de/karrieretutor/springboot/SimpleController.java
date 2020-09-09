package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.ProduktRepository;
import de.karrieretutor.springboot.domain.Warenkorb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping(value = "/")
public class SimpleController {
    Logger LOG = LoggerFactory.getLogger(SimpleController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    ProduktRepository produktRepository;
    public Warenkorb warenkorb = new Warenkorb();
    List<Produkt> produkte = new ArrayList<>();

    @GetMapping("/")
    public String homePage(Model model) {
        return "redirect:/index.html";
    }

    @GetMapping("/app")
    public String app() {
        return "redirect:/app/index.html";
    }

    @GetMapping("/index.html")
    public String shop(Model model, Locale locale) {
        model.addAttribute("titel", "Index");
        if (produkte.isEmpty()) {
            produktRepository.findAll().forEach(produkte::add);
        }
        model.addAttribute("produkte", produkte);
        model.addAttribute("warenkorb", this.warenkorb);
        return "index";
    }

    @GetMapping("/{name}.html")
    public String htmlMapping(@PathVariable(name = "name") String name, Model model) {
        model.addAttribute("titel", StringUtils.capitalize(name));
        model.addAttribute("warenkorb", this.warenkorb);
        return name;
    }

    @GetMapping("/fotos/{id}")
    public ResponseEntity<Resource> fotos(@PathVariable Long id) throws IOException, URISyntaxException {
        byte[] bytes = new byte[0];
        if (id != null) {
            Produkt produkt = getProdukt(id);
            if (produkt != null) {
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

    @GetMapping("/kaufen")
    public String kaufen(@RequestParam Long id, Model model, RedirectAttributes redirect, Locale locale) {
        String message = messageSource.getMessage("cart.product.id.not.found", new String[]{String.valueOf(id)}, locale);
        if (id != null) {
            Produkt produkt = getProdukt(id);
            if (produkt != null) {
                warenkorb.getProdukte().add(produkt);
                message = messageSource.getMessage("cart.added", new String[]{produkt.getName()}, locale);
            }
        }
        redirect.addFlashAttribute("message", message);
        return "redirect:index.html";
    }

    @GetMapping("/entfernen")
    public String entfernen(@RequestParam Long id, Model model, RedirectAttributes redirect, Locale locale) {
        String message = messageSource.getMessage("cart.product.id.not.found", new Object[]{id}, locale);
        if (id != null) {
            Produkt gefundenesProdukt = null;
            for (Produkt p : warenkorb.getProdukte()) {
                if (id.equals(p.getId())) {
                    gefundenesProdukt = p;
                    break;
                }
            }
            if (gefundenesProdukt != null) {
                warenkorb.getProdukte().remove(gefundenesProdukt);
                message = messageSource.getMessage("cart.removed", new Object[]{gefundenesProdukt.getName()}, locale);
            } else {
                message = messageSource.getMessage("cart.not.found", new String[]{String.valueOf(id)}, locale);
            }
        }
        redirect.addFlashAttribute("message", message);
        model.addAttribute("titel", "Warenkorb");
        model.addAttribute("warenkorb", warenkorb);
        return "redirect:/warenkorb.html";
    }

    private Produkt getProdukt(Long id) {
        Optional<Produkt> produktDB;
        if (!produkte.isEmpty()) {
            LOG.debug("loading from Cache");
            produktDB = produkte.stream().filter(p -> p.getId() == id).findFirst();
        } else {
            LOG.debug("loading from Repository");
            produktDB = produktRepository.findById(id);
        }
        return produktDB.isPresent() ? produktDB.get() : null;
    }
}
