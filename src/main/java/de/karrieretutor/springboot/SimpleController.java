package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.ProduktRepository;
import de.karrieretutor.springboot.domain.Warenkorb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Controller
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
        return name;
    }

    @GetMapping("/bearbeiten.html")
    public String bearbeiten(@RequestParam(required = false, name = "id") Long id, Model model) {
        Produkt aktuellesProdukt = new Produkt();
        if (id != null) {
            Optional<Produkt> produktDB = produktRepository.findById(id);
            if (produktDB.isPresent()) {
                aktuellesProdukt = produktDB.get();
            }
        }
        model.addAttribute("titel", "bearbeiten");
        model.addAttribute("produkt", aktuellesProdukt);
        return "bearbeiten";
    }

    @PostMapping("/speichern")
    public String speichern(@Valid Produkt produkt, BindingResult fields, Model model) {
        if (fields.hasErrors()) {
            return "bearbeiten";
        }
        produktRepository.save(produkt);
        model.addAttribute("produkte", produktRepository.findAll());
        return "redirect:/index.html";
    }

    @GetMapping("/loeschen")
    public String loeschen(@RequestParam Long id, Model model) {
        if (id != null) {
            Optional<Produkt> produktDB = produktRepository.findById(id);
            if (produktDB.isPresent()) {
                produktRepository.delete(produktDB.get());
            }
        }
        return "redirect:/index.html";
    }

    @GetMapping("/warenkorb.html")
    public String ladeWarenkorb(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            // TODO: lade Warenkorb aus der Datenbank
        }
        model.addAttribute("titel", "Warenkorb");
        model.addAttribute("warenkorb", this.warenkorb);
        return "warenkorb";
    }

    @GetMapping("/kaufen")
    public String kaufen(@RequestParam Long id, Model model) {
        Produkt aktuellesProdukt = new Produkt();
        if (id != null) {
            Optional<Produkt> produktDB = produktRepository.findById(id);
            if (produktDB.isPresent()) {
                aktuellesProdukt = produktDB.get();
                warenkorb.getProdukte().add(aktuellesProdukt);
                model.addAttribute("message", "Produkt \'" + aktuellesProdukt.getName() + "\' zum Warenkorb hinzugefügt.");
            }
        } else {
            model.addAttribute("message", "Produkt mit der ID \'" + id + "\' nicht gefunden.");
        }
        model.addAttribute("titel", "Warenkorb");
        model.addAttribute("warenkorb", warenkorb);
        return "warenkorb";
    }

    @GetMapping("/entfernen")
    public String entfernen(@RequestParam Long id, Model model) {
        if (id != null) {
            AtomicReference<Produkt> gefunden = new AtomicReference<>();
            warenkorb.getProdukte().forEach(aktuellesProdukt -> {
                if (id.equals(aktuellesProdukt.getId())) {
                    gefunden.set(aktuellesProdukt);
                    return;
                }
            });
            Produkt gefundenesProdukt = gefunden.get();
            if (gefundenesProdukt != null) {
                warenkorb.getProdukte().remove(gefundenesProdukt);
                model.addAttribute("message", "Produkt \'" + gefundenesProdukt.getName() + "\' vom Warenkorb entfernt.");
            } else {
                model.addAttribute("message", "Produkt mit ID \'" + id + "\' nicht im Warenkorb gefunden.");
            }
        } else {
            model.addAttribute("message", "Produkt nicht gefunden.");
        }
        model.addAttribute("titel", "Warenkorb");
        model.addAttribute("warenkorb", warenkorb);
        return "warenkorb";
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", sw.toString());
        mav.setViewName("error");
        return mav;
    }


}
