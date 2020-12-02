package de.karrieretutor.springboot;

import de.karrieretutor.springboot.model.Produkt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping(value = "/admin/")
public class AdminController {
    @Autowired
    SimpleController sc;
    @Autowired
    MessageSource messageSource;

    @GetMapping("bearbeiten.html")
    public String bearbeiten(@RequestParam(required = false) Long id,
                             Model model) {
        Produkt aktuellesProdukt = (id != null) ? findById(id) : new Produkt();
        model.addAttribute("produkt", aktuellesProdukt);
        return "admin/bearbeiten";
    }

    @PostMapping("speichern")
    public String speichern(@Valid Produkt produkt,
                            BindingResult fields,
                            Model model,
                            RedirectAttributes redirect,
                            Locale locale) {
        if (fields.hasErrors()) {
            return "admin/bearbeiten";
        }
        saveProdukt(produkt);
        String message = messageSource.getMessage("product.saved", new Object[]{produkt.getName()}, locale);
        redirect.addFlashAttribute("message", message);
        model.addAttribute("produkte", ladeProdukte());
        return "redirect:/index.html";
    }

    @GetMapping("loeschen")
    public String loeschen(@RequestParam Long id,
                           RedirectAttributes redirect,
                           Locale locale) {
        if (id != null) {
            Produkt produkt = findById(id);
            if (produkt != null) {
                String message = messageSource.getMessage("product.deleted", new Object[]{produkt.getName()}, locale);
                try {
                    deleteProdukt(produkt);
                } catch (Exception e) {
                    message = messageSource.getMessage("product.delete.not.possible", new Object[]{produkt.getName()}, locale);
                }
                redirect.addFlashAttribute("message", message);
            }
        }
        return "redirect:/index.html";
    }

    private Produkt findById(Long produktId) {
        List<Produkt> produkte = ladeProdukte();
        if (produkte == null)
            return null;
        return produkte.stream().filter(p -> p.getId() == produktId).findFirst().orElse(null);
    }

    private List<Produkt> ladeProdukte() {
        return sc.produkte;
    }

    private void saveProdukt(Produkt neuesProdukt) {
        if (neuesProdukt.getId() == null) {
            sc.produkte.add(neuesProdukt);
        } else {
            Produkt vorhandenesProdukt = findById(neuesProdukt.getId());
            int index = sc.produkte.indexOf(vorhandenesProdukt);
            sc.produkte.set(index, neuesProdukt);
        }
    }

    private void deleteProdukt(Produkt produkt) {
        sc.produkte.remove(produkt);
    }
}
