package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.ProduktRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping(value = "/admin/")
public class AdminController {
    @Autowired
    ProduktRepository produktRepository;

    @GetMapping("bearbeiten.html")
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
        return "admin/bearbeiten";
    }

    @PostMapping("speichern")
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

    @GetMapping("loeschen")
    public String loeschen(@RequestParam Long id, Model model, RedirectAttributes redirect) {
        if (id != null) {
            Optional<Produkt> produktDB = produktRepository.findById(id);
            if (produktDB.isPresent()) {
                Produkt produkt = produktDB.get();
                redirect.addFlashAttribute("message", "Produkt \"" + produkt.getName() + "\" gelöscht.");
                produktRepository.delete(produkt);
            }
        }
        return "redirect:/index.html";
    }
}
