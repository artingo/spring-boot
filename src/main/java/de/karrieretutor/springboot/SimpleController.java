package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Wein;
import de.karrieretutor.springboot.domain.WeinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static de.karrieretutor.springboot.domain.Kategorie.ROTWEIN;
import static de.karrieretutor.springboot.domain.Kategorie.WEISSWEIN;
import static de.karrieretutor.springboot.domain.Rebsorte.*;

@Controller
public class SimpleController {
    @Value("${spring.application.name}")
    String appName;

    @Autowired
    WeinRepository weinRepository;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("titel", "Freddie Walker");
        return "index";
    }

    @GetMapping("/{name}.html")
    public String htmlMapping(@PathVariable(name = "name") String name, Model model) {
        model.addAttribute("titel", StringUtils.capitalize(name));
//        List<Wein> weine = createWeine();
//        model.addAttribute("weine", weine);
//        model.addAttribute("weine", weinRepository.findAll());
        model.addAttribute("preis", 9.90);
        return name;
    }

    @GetMapping("/wein-hinzufuegen.html")
    public String weinHinzufuegen(Model model) {
        Wein neuerWein = new Wein();
        model.addAttribute("wein", neuerWein);
        return "wein-hinzufuegen";
    }

    @PostMapping("/weinHinzufuegen")
    public String weinHinzufuegen(@Valid Wein wein, BindingResult fields, Model model) {
        if (fields.hasErrors()) {
            return "wein-hinzufuegen";
        }
        weinRepository.save(wein);
        model.addAttribute("weine", weinRepository.findAll());
        return "redirect:/index.html";
    }

    private List<Wein> createWeine() {
        List<Wein> weine = new ArrayList<>();
        weine.add(new Wein("Kloster Eberbach", "Rheingau", WEISSWEIN, RIESLING));
        weine.add(new Wein("Ernest & Julio Gallo", "Kalifornien", ROTWEIN, ZINFANDEL));
        weine.add(new Wein("Mia", "Deutschland", WEISSWEIN, SILVANER));
        return weine;
    }
}
