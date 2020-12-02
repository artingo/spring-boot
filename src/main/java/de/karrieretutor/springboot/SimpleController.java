package de.karrieretutor.springboot;

import de.karrieretutor.springboot.model.Produkt;
import de.karrieretutor.springboot.model.Warenkorb;
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

import static de.karrieretutor.springboot.model.Kategorie.*;
import static de.karrieretutor.springboot.model.Unterkategorie.*;

@Controller
public class SimpleController {
    Warenkorb warenkorb = new Warenkorb();
    public List<Produkt> produkte = createProdukte();

    @Autowired
    MessageSource messageSource;

    @GetMapping("/")
    public String homePage(Model model) {
        return "redirect:/index.html";
    }

    @GetMapping("/{name}.html")
    public String htmlMapping(@PathVariable(name = "name") String name, Model model) {
        model.addAttribute("produkte", produkte);
        model.addAttribute("warenkorb", warenkorb);
        return name;
    }

    @GetMapping("/kaufen")
    public String kaufen(@RequestParam Long id, Model model, RedirectAttributes redirect, Locale locale) {
        String message = messageSource.getMessage("cart.product.id.not.found", new String[]{String.valueOf(id)}, locale);
        if (id != null) {
            Produkt produkt = findProduktById(id);
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
        String message = messageSource .getMessage("cart.not.found", new String[]{String.valueOf(id)}, locale);
        Produkt entferntesProdukt = warenkorb.produktEntfernen(id);
        if (entferntesProdukt != null) {
            message = messageSource.getMessage("cart.removed", new Object[]{entferntesProdukt.getName()}, locale);
        }
        redirect.addFlashAttribute("message", message);
        model.addAttribute("titel", "Warenkorb");
        model.addAttribute("warenkorb", warenkorb);
        return "redirect:warenkorb.html";
    }

    private List<Produkt> createProdukte() {
        List<Produkt> produkte = new ArrayList<>();
        produkte.add(new Produkt("Château-Neuf du Pape", "Rheingau", KAT1, SUBKAT1, 1077.99));
        produkte.add(new Produkt("Ernest & Julio Gallo", "Kalifornien", KAT2, SUBKAT2, 4.99));
        produkte.add(new Produkt("Rioja", "Deutschland", KAT3, SUBKAT3, 6.99));
        produkte.add(new Produkt("Rotkäppchen", "Sachsen", KAT4, SUBKAT4, 3.99));
        return produkte;
    }

    private Produkt findProduktById(Long produktId) {
        return produkte.stream().filter(produkt -> produkt.getId() == produktId).findFirst().orElse(null);
    }
}
