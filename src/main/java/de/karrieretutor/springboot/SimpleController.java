package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Wein;
import de.karrieretutor.springboot.domain.WeinRepository;

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
    WeinRepository weinRepository;

    @GetMapping("/")
    public String homePage(Model model) {
        return "redirect:/index.html";
    }

    @GetMapping("/{name}.html")
    public String htmlMapping(@PathVariable(name = "name") String name, Model model) {
        model.addAttribute("titel", StringUtils.capitalize(name));
        model.addAttribute("weine", weinRepository.findAll());
        return name;
    }

    @GetMapping("/wein-bearbeiten.html")
    public String weinBearbeiten(@RequestParam(required = false, name = "id") Long id, Model model) {
        Wein aktuellerWein = new Wein();
        if (id != null) {
            Optional<Wein> vorhandenerWein = weinRepository.findById(id);
            if (vorhandenerWein.isPresent()) {
                aktuellerWein = vorhandenerWein.get();
            }
        }
        model.addAttribute("titel", "wein-bearbeiten");
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
