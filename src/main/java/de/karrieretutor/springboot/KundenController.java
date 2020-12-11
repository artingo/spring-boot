package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Kunde;
import de.karrieretutor.springboot.domain.Warenkorb;
import de.karrieretutor.springboot.dto.Login;
import de.karrieretutor.springboot.service.KundenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping(value = "/kunde/")
public class KundenController {
    @Autowired
    KundenService kundenService;
    @Autowired
    MessageSource messageSource;
    @Autowired
    SimpleController sc;

    // TODO: aus der Session holen
    @ModelAttribute("warenkorb")
    public Warenkorb getInitializedWarenkorb() {
        return sc.warenkorb;
    }

    @GetMapping("/")
    public String kundenDetails(Model model, HttpSession session) {
        Kunde kunde = (Kunde) session.getAttribute("kunde");
        if (kunde != null) {
            model.addAttribute("kunde", kunde);
            return "customer";
        }
        model.addAttribute("login", new Login());
        return "login";
    }

    @GetMapping("login")
    public String loginSeite(Model model) {
        model.addAttribute("login", new Login());
        return "login";
    }

    @PostMapping("login")
    public String login(@Valid Login login,
                        BindingResult result,
                        HttpSession session,
                        RedirectAttributes redirect,
                        Locale locale) {
        if (result.hasErrors()) {
            return "login";
        }
        Kunde kunde = kundenService.findByEmail(login.getEmail());
        String message;
        if (kunde == null) {
            result.rejectValue("email","customer.not.found", new String[]{login.getEmail()}, "customer.not.found");
            return "login";
        }
        if (!login.getPassword().equals(kunde.getPassword())) {
            result.rejectValue("password", "validation.password.wrong");
            return "login";
        }
        message = messageSource.getMessage("customer.logged.in", new String[]{kunde.getNameFormatiert()}, locale);
        redirect.addFlashAttribute("message", message);
        session.setAttribute("kunde", kunde);
        return "redirect:/index.html";
    }

    @GetMapping("logout")
    public String logout(HttpSession session, RedirectAttributes redirect, Locale locale) {
        session.removeAttribute("kunde");
        sc.warenkorb.getProdukte().clear();
        String message = messageSource.getMessage("customer.logged.out", null, locale);
        redirect.addFlashAttribute("message", message);
        return "redirect:/index.html";
    }

    @GetMapping("registrieren")
    public String registrierSeite(Model model) {
        model.addAttribute("login", new Login(true));
        return "login";
    }

    @PostMapping("registrieren")
    public String registrieren(@Valid Login login,
                               BindingResult result,
                               RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "login";
        }
        Kunde kunde = kundenService.findByEmail(login.getEmail());
        if (kunde != null) {
            result.rejectValue("email", "validation.register.email.reserved");
            return "login";
        }
        if (!login.getPassword().equals(login.getPasswordRepeat())) {
            result.rejectValue("passwordRepeat", "validation.password.repeat");
            return "login";
        }

        Kunde neuerKunde = new Kunde(login.getEmail(), login.getPassword());
        redirect.addFlashAttribute("kunde", neuerKunde);
        return "redirect:/customer.html";
    }

    @PostMapping("speichern")
    public String speichern(@Valid Kunde kunde,
                            BindingResult result,
                            RedirectAttributes redirect,
                            HttpSession session,
                            Locale locale) {
        if (result.hasErrors()) {
            return "customer";
        }
        Kunde gespeicherterKunde = kundenService.speichern(kunde);
        String message = messageSource.getMessage("customer.failure", null, locale);
        if (gespeicherterKunde != null) {
            session.setAttribute("kunde", gespeicherterKunde);
            message = messageSource.getMessage("customer.success", null, locale);
        }
        redirect.addFlashAttribute("message", message);
        return "redirect:/index.html";
    }
}
