package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.ProduktRepository;
import de.karrieretutor.springboot.domain.Warenkorb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

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
    public String speichern(@RequestParam MultipartFile file,
                            @Valid Produkt produkt, BindingResult fields,
                            Model model, RedirectAttributes redirect) throws IOException {
        if (fields.hasErrors()) {
            return "bearbeiten";
        }
        produkt.setDateiname(file.getOriginalFilename());
        produkt.setDatei(file.getBytes());
        produktRepository.save(produkt);

        redirect.addFlashAttribute("message", "Produkt \"" + produkt.getName() + "\" gespeichert.");
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

    @GetMapping("/fotos/{id}")
    public ResponseEntity<Resource> fotos(@PathVariable Long id) {
        Produkt produkt = new Produkt();
        if (id != null) {
            Optional<Produkt> produktDB = produktRepository.findById(id);
            if (produktDB.isPresent()) {
                produkt = produktDB.get();
            }
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new ByteArrayResource(produkt.getDatei()));
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
    public String kaufen(@RequestParam Long id, Model model, RedirectAttributes redirect) {
        String message = "Produkt mit der ID \"" + id + "\" nicht gefunden.";
        if (id != null) {
            Optional<Produkt> produktDB = produktRepository.findById(id);
            if (produktDB.isPresent()) {
                Produkt aktuellesProdukt = produktDB.get();
                warenkorb.getProdukte().add(aktuellesProdukt);
                message = "Produkt \"" + aktuellesProdukt.getName() + "\" zum Warenkorb hinzugefügt.";
            }
        }
        redirect.addFlashAttribute("message", message);
        return "redirect:index.html";
    }

    @GetMapping("/entfernen")
    public String entfernen(@RequestParam Long id, Model model, RedirectAttributes redirect) {
        String message = "Produkt nicht gefunden.";
        if (id != null) {
            Produkt gefundenesProdukt = warenkorb.getProdukte().stream()
                    .filter(p -> id.equals(p.getId()))
                    .findFirst().get();
            if (gefundenesProdukt != null) {
                warenkorb.getProdukte().remove(gefundenesProdukt);
                message = "Produkt \"" + gefundenesProdukt.getName() + "\" vom Warenkorb entfernt.";
            } else {
                message = "Produkt mit ID \"" + id + "\" nicht im Warenkorb gefunden.";
            }
        }
        redirect.addFlashAttribute("message", message);
        model.addAttribute("titel", "Warenkorb");
        model.addAttribute("warenkorb", warenkorb);
        return "redirect:/warenkorb.html";
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
