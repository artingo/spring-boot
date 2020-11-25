package de.karrieretutor.springboot;

import de.karrieretutor.springboot.model.Produkt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static de.karrieretutor.springboot.model.Kategorie.*;
import static de.karrieretutor.springboot.model.Unterkategorie.*;

@Controller
public class SimpleController {
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        return "redirect:/index.html";
    }

    @GetMapping("/{name}.html")
    public String htmlMapping(@PathVariable(name = "name") String name, Model model) {
        model.addAttribute("titel", StringUtils.capitalize(name));
        List<Produkt> produkte = createProdukte();
        model.addAttribute("produkte", produkte);
        model.addAttribute("appName", appName);
        return name;
    }

    private List<Produkt> createProdukte() {
        List<Produkt> produkte = new ArrayList<>();
        produkte.add(new Produkt("Kloster Eberbach", "Rheingau", KAT1, SUBKAT1, 7.99));
        produkte.add(new Produkt("Ernest & Julio Gallo", "Kalifornien", KAT2, SUBKAT2, 4.99));
        produkte.add(new Produkt("Rioja", "Deutschland", KAT3, SUBKAT3, 6.99));
        return produkte;
    }
}
