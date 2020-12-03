package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.Warenkorb;
import de.karrieretutor.springboot.service.ProduktService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static de.karrieretutor.springboot.enums.Kategorie.*;
import static de.karrieretutor.springboot.enums.Unterkategorie.*;

@Controller
public class SimpleController {
    @Autowired
    ProduktService produktService;

    @Autowired
    MessageSource messageSource;

    Warenkorb warenkorb = new Warenkorb();

    @GetMapping("/")
    public String homePage(Model model) {
        return "redirect:/index.html";
    }

    @GetMapping("/index.html")
    public String shop(Model model, Locale locale) {
        model.addAttribute("produkte", produktService.ladeProdukte());
        model.addAttribute("warenkorb", this.warenkorb);
        return "index";
    }

    @GetMapping("/{name}.html")
    public String htmlMapping(@PathVariable(name = "name") String name, Model model) {
        model.addAttribute("warenkorb", this.warenkorb);
        return name;
    }

    @GetMapping("/kaufen")
    public String kaufen(@RequestParam Long id,
                         RedirectAttributes redirect,
                         Locale locale) {
        String message = messageSource.getMessage("cart.product.id.not.found", new String[]{String.valueOf(id)}, locale);
        if (id != null) {
            Produkt produkt = produktService.getProdukt(id);
            if (produkt != null) {
                warenkorb.produktHinzufuegen(produkt);
                message = messageSource.getMessage("cart.added", new String[]{produkt.getName()}, locale);
            }
        }
        redirect.addFlashAttribute("message", message);
        return "redirect:index.html";
    }

    @GetMapping("/entfernen")
    public String entfernen(@RequestParam Long id, Model model, RedirectAttributes redirect, Locale locale) {
        String message = messageSource .getMessage("cart.not.found", new Long[]{id}, locale);
        Produkt entferntesProdukt = warenkorb.produktEntfernen(id);
        if (entferntesProdukt != null) {
            message = messageSource.getMessage("cart.removed", new String[]{entferntesProdukt.getName()}, locale);
        }
        redirect.addFlashAttribute("message", message);
        model.addAttribute("warenkorb", warenkorb);
        return "redirect:/warenkorb.html";
    }
}
