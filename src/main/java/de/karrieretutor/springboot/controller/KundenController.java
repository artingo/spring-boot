package de.karrieretutor.springboot.controller;

import de.karrieretutor.springboot.domain.Kunde;
import de.karrieretutor.springboot.domain.Warenkorb;
import de.karrieretutor.springboot.dto.Login;
import de.karrieretutor.springboot.service.EmailService;
import de.karrieretutor.springboot.service.KundenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Locale;

import static de.karrieretutor.springboot.Const.*;

@Controller
@RequestMapping(value = "/kunde/")
public class KundenController {
    @Autowired
    KundenService kundenService;
    @Autowired
    MessageSource messageSource;
    @Autowired
    EmailService emailService;

    @GetMapping("/")
    public String kundenDetails(Model model, HttpSession session) {
        Kunde kunde = (Kunde) session.getAttribute(CUSTOMER);
        if (kunde != null) {
            model.addAttribute(CUSTOMER, kunde);
            return "customer";
        }
        model.addAttribute(LOGIN, new Login());
        return "login";
    }

    @GetMapping("login")
    public String loginSeite(Model model) {
        model.addAttribute(LOGIN, new Login());
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
        redirect.addFlashAttribute(MESSAGE, message);
        session.setAttribute(CUSTOMER, kunde);
        Warenkorb warenkorb = (Warenkorb) session.getAttribute(CART);
        String redirectURL = (warenkorb != null && !warenkorb.getProdukte().isEmpty()) ? "checkout" : "index.html";
        return "redirect:/" + redirectURL;
    }

    @GetMapping("logout")
    public String logout(HttpSession session, RedirectAttributes redirect, Locale locale) {
        session.removeAttribute(CUSTOMER);
        Warenkorb warenkorb = (Warenkorb)session.getAttribute(CART);
        if (warenkorb != null)
            warenkorb.getProdukte().clear();
        String message = messageSource.getMessage("customer.logged.out", null, locale);
        redirect.addFlashAttribute(MESSAGE, message);
        return "redirect:/index.html";
    }

    @GetMapping("registrieren")
    public String registrierSeite(Model model) {
        model.addAttribute(LOGIN, new Login(true));
        return "login";
    }

    @PostMapping("registrieren")
    public String registrieren(@Valid Login login,
                               BindingResult result,
                               RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "login";
        }
        if (kundenService.findByEmail(login.getEmail()) != null) {
            result.rejectValue("email", "validation.register.email.reserved");
            return "login";
        }
        if (!login.getPassword().equals(login.getPasswordRepeat())) {
            result.rejectValue("passwordRepeat", "validation.password.repeat");
            return "login";
        }

        Kunde neuerKunde = new Kunde(login.getEmail(), login.getPassword());
        redirect.addFlashAttribute(CUSTOMER, neuerKunde);
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
        Kunde vorhandenerKunde = kundenService.findByEmail(kunde.getEmail());
        if (vorhandenerKunde != null && vorhandenerKunde.getId() != kunde.getId()) {
            result.rejectValue("email", "validation.register.email.reserved");
            return "customer";
        }

        Kunde gespeicherterKunde = kundenService.speichern(kunde);
        String message = messageSource.getMessage("customer.failure", null, locale);
        if (gespeicherterKunde != null) {
            session.setAttribute(CUSTOMER, gespeicherterKunde);
            message = messageSource.getMessage("customer.success", null, locale);
            emailService.kundenDatenGeandert(gespeicherterKunde);

        }
        redirect.addFlashAttribute(MESSAGE, message);
        return "redirect:/index.html";
    }
}
