package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.ProduktRepository;

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

@Controller
public class SimpleController {
    @Autowired
    ProduktRepository produktRepository;

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
