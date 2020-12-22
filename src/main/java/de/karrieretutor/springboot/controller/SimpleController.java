package de.karrieretutor.springboot.controller;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.Warenkorb;
import de.karrieretutor.springboot.service.ProduktService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

import static de.karrieretutor.springboot.Const.*;

@Controller
public class SimpleController {
    @Autowired
    ProduktService produktService;
    @Autowired
    MessageSource messageSource;
    @Autowired
    Warenkorb warenkorb;

    @GetMapping("/")
    public String homePage(Model model) {
        return "redirect:/index.html";
    }

    @GetMapping("/index.html")
    public String shop(Model model, HttpSession session) {
        model.addAttribute(PRODUCTS, produktService.ladeProdukte());
        session.setAttribute(CART, warenkorb);
        return "index";
    }

    @GetMapping("/{name}.html")
    public String htmlMapping(@PathVariable(name = "name") String name, HttpSession session) {
        session.setAttribute(CART, warenkorb);
        return name;
    }

    @GetMapping("/fotos/{id}")
    public ResponseEntity<Resource> fotos(@PathVariable Long id) throws IOException, URISyntaxException {
        byte[] bytes;
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

    @GetMapping("/kaufen")
    public String kaufen(@RequestParam Long id,
                         RedirectAttributes redirect,
                         Locale locale,
                         HttpSession session) {
        String message = messageSource.getMessage("cart.product.id.not.found", new String[]{String.valueOf(id)}, locale);
        if (id != null) {
            Produkt produkt = produktService.getProdukt(id);
            if (produkt != null) {
                warenkorb.produktHinzufuegen(produkt);
                session.setAttribute(CART, warenkorb);
                message = messageSource.getMessage("cart.added", new String[]{produkt.getName()}, locale);
            }
        }
        redirect.addFlashAttribute(MESSAGE, message);
        return "redirect:index.html";
    }

    @GetMapping("/entfernen")
    public String entfernen(@RequestParam Long id,
                            Model model,
                            RedirectAttributes redirect,
                            Locale locale,
                            HttpSession session) {
        String message = messageSource.getMessage("cart.not.found", new Long[]{id}, locale);
        Produkt entferntesProdukt = warenkorb.produktEntfernen(id);
        if (entferntesProdukt != null) {
            session.setAttribute(CART, warenkorb);
            message = messageSource.getMessage("cart.removed", new String[]{entferntesProdukt.getName()}, locale);
        }
        redirect.addFlashAttribute(MESSAGE, message);
        model.addAttribute(CART, warenkorb);
        return "redirect:/cart.html";
    }
}
