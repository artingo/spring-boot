package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.service.ProduktService;
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
import java.util.Locale;

@Controller
@RequestMapping(value = "/admin/")
public class AdminController {
    @Autowired
    ProduktService produktService;

    @Autowired
    MessageSource messageSource;

    @GetMapping("bearbeiten.html")
    public String bearbeiten(@RequestParam(required = false) Long id,
                             Model model) {
        Produkt aktuellesProdukt = new Produkt();
        if (id != null) {
            Produkt vorhandenesProdukt = produktService.getProdukt(id);
            if (vorhandenesProdukt != null)
                aktuellesProdukt = vorhandenesProdukt;
        }
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
        produktService.updateProdukt(produkt);
        String message = messageSource.getMessage("product.saved", new Object[]{produkt.getName()}, locale);
        redirect.addFlashAttribute("message", message);
        model.addAttribute("produkte", produktService.ladeProdukte());
        return "redirect:/index.html";
    }

    @GetMapping("loeschen")
    public String loeschen(@RequestParam(required = false) Long id,
                           RedirectAttributes redirect,
                           Locale locale) {
        String message = messageSource.getMessage("cart.product.id.not.found", new Long[]{id}, locale);
        try {
            String produktName = produktService.deleteProdukt(id);
            if (produktName != null)
                message = messageSource.getMessage("product.deleted", new String[]{produktName}, locale);
        } catch (Exception e) {
            message = messageSource.getMessage("product.delete.not.possible", new Long[]{id}, locale);
        }
        redirect.addFlashAttribute("message", message);
        return "redirect:/index.html";
    }
}
