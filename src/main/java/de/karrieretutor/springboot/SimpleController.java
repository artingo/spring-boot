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
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

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
        model.addAttribute("weine", weinRepository.findAll());
        return name;
    }

    @GetMapping("/wein-bearbeiten.html")
    public String weinBearbeiten(@RequestParam(required = false) Long id, Model model) {
        Wein aktuellerWein = new Wein();
        if (id != null) {
            Optional<Wein> vorhandenerWein = weinRepository.findById(id);
            if (vorhandenerWein.isPresent()) {
                aktuellerWein = vorhandenerWein.get();
            }
        }
        model.addAttribute("wein", aktuellerWein);
        return "wein-bearbeiten";
    }

    @PostMapping("/weinSpeichern")
    public String weinSpeichern(@Valid Wein wein, BindingResult fields, Model model) {
        if (fields.hasErrors()) {
            return "wein-bearbeiten";
        }
        weinRepository.save(wein);
        model.addAttribute("weine", weinRepository.findAll());
        return "redirect:/index.html";
    }

    @GetMapping("/weinLoeschen")
    public String weinLoeschen(@RequestParam Long id, Model model) {
        if (id != null) {
            Optional<Wein> vorhandenerWein = weinRepository.findById(id);
            if (vorhandenerWein.isPresent()) {
                weinRepository.delete(vorhandenerWein.get());
            }
        }
        return "redirect:/index.html";
    }
}
