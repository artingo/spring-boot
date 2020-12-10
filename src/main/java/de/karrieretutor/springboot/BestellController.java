package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Bestellung;
import de.karrieretutor.springboot.domain.Kunde;
import de.karrieretutor.springboot.service.BestellService;
import de.karrieretutor.springboot.service.KundenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
public class BestellController {
    @Autowired
    BestellService bestellService;
    @Autowired
    KundenService kundenService;
    @Autowired
    MessageSource messageSource;
    @Autowired
    SimpleController sc;

    @GetMapping("/checkout")
    public String zurKasse(@RequestParam(required = false) Long id, Model model) {
        Kunde kunde = kundenService.lade(id);
        Bestellung bestellung = new Bestellung();
        bestellung.setKunde(kunde);
        model.addAttribute("bestellung", bestellung);
        model.addAttribute("warenkorb", sc.warenkorb);
        return "checkout";
    }

    @PostMapping("/bestellen")
    public String bestellen(@Valid Bestellung bestellung,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirect,
                            Locale locale) {
        model.addAttribute("warenkorb", sc.warenkorb);
        if (result.hasErrors()) {
            return "checkout";
        }
        // TODO: kunde.validiereZahlungsart(result) implementieren
        Kunde kunde = bestellung.getKunde();
        switch (kunde.getZahlungsart()) {
            case EINZUG:
                if (StringUtils.isEmptyOrWhitespace(kunde.getIban())) {
                    result.rejectValue("kunde.iban", "validation.zahlungsart.iban");
                    return "checkout";
                }
                break;
            case KREDITKARTE:
                if (StringUtils.isEmptyOrWhitespace(kunde.getKreditkartenNr())) {
                    result.rejectValue("kunde.kreditkartenNr", "validation.zahlungsart.karte");
                    return "checkout";
                }
        }

        String message = messageSource.getMessage("order.failure", null, locale);
        Bestellung neueBestellung = bestellService.speichere(bestellung, true);
        if (neueBestellung != null) {
            message = messageSource.getMessage("order.success", null, locale);
        }
        redirect.addFlashAttribute("message", message);
        return "redirect:/bestellung/" + neueBestellung.getId();
    }

    @GetMapping("/bestellung/{id}")
    public String bestellung(@PathVariable Long id, Model model) {
        Bestellung bestellung = bestellService.lade(id);
        // TODO: User-Berechtigung prüfen
        if (bestellung == null) {
            bestellung = new Bestellung();
            model.addAttribute("message", "No order found for ID: " + id);
        }
        model.addAttribute("bestellung", bestellung);
        model.addAttribute("warenkorb", sc.warenkorb);
        return "order";
    }

    @GetMapping("/bestellungen/{kundenId}")
    public String bestellungen(@PathVariable Long kundenId, Model model, HttpSession session) {
        session.getAttribute("kundenId");
        List<Bestellung> bestellungen = bestellService.bestellungenVonKunde(kundenId);
        // TODO: User-Berechtigung prüfen
        if (bestellungen.isEmpty()) {
            model.addAttribute("message", "No orders found for customer ID: " + kundenId);
        }
        model.addAttribute("bestellungen", bestellungen);
        model.addAttribute("warenkorb", sc.warenkorb);
        return "orders";
    }


}
