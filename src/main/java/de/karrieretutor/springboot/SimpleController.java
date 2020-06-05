package de.karrieretutor.springboot;

import de.karrieretutor.springboot.model.Wein;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static de.karrieretutor.springboot.model.Kategorie.ROTWEIN;
import static de.karrieretutor.springboot.model.Kategorie.WEISSWEIN;
import static de.karrieretutor.springboot.model.Rebsorte.*;

@Controller
public class SimpleController {
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("titel", "Freddie Walker");
        return "index";
    }

    @GetMapping("/{name}.html")
    public String htmlMapping(@PathVariable(name = "name") String name, Model model) {
        model.addAttribute("titel", StringUtils.capitalize(name));
        List<Wein> weine = createWeine();
        model.addAttribute("weine", weine);
        model.addAttribute("preis", 9.90);
        return name;
    }

    private List<Wein> createWeine() {
        List<Wein> weine = new ArrayList<>();
        weine.add(new Wein("Kloster Eberbach", "Rheingau", WEISSWEIN, RIESLING));
        weine.add(new Wein("Ernest & Julio Gallo", "Kalifornien", ROTWEIN, ZINFANDEL));
        weine.add(new Wein("Mia", "Deutschland", WEISSWEIN, SILVANER));
        return weine;
    }
}
