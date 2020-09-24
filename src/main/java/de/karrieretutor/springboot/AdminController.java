package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.ProduktRepository;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping(value = "/admin/")
public class AdminController {
    @Autowired
    ProduktService produktService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    ProduktRepository produktRepository;

    @GetMapping("bearbeiten.html")
    public String bearbeiten(@RequestParam(required = false, name = "id") Long id, Model model) {
        Produkt aktuellesProdukt = (id != null)? produktService.getProdukt(id) : new Produkt();
        model.addAttribute("titel", "bearbeiten");
        model.addAttribute("produkt", aktuellesProdukt);
        return "admin/bearbeiten";
    }

    @PostMapping("speichern")
    public String speichern(@RequestParam MultipartFile file,
                            @Valid Produkt produkt, BindingResult fields,
                            Model model, RedirectAttributes redirect,
                            Locale locale) throws IOException {
        if (fields.hasErrors()) {
            // TODO: File mitliefern
            return "admin/bearbeiten";
        }
        produkt.setDateiname(file.getOriginalFilename());
        produkt.setDatei(file.getBytes());
        Produkt neuesProdukt = produktRepository.save(produkt);
        produktService.updateProdukt(neuesProdukt);

        String message = messageSource.getMessage("product.saved", new Object[]{produkt.getName()}, locale);
        redirect.addFlashAttribute("message", message);
        model.addAttribute("produkte", produktService.ladeProdukte());
        return "redirect:/index.html";
    }

    @GetMapping("loeschen")
    public String loeschen(@RequestParam Long id, Model model,
                           RedirectAttributes redirect,
                           Locale locale) {
        if (id != null) {
            Optional<Produkt> produktDB = produktRepository.findById(id);
            if (produktDB.isPresent()) {
                Produkt produkt = produktDB.get();
                String message = messageSource.getMessage("product.deleted", new Object[]{produkt.getName()}, locale);
                try {
                    produktRepository.delete(produkt);
                    produktService.deleteProdukt(id);
                } catch(Exception e) {
                    message = messageSource.getMessage("product.delete.not.possible", new Object[]{produkt.getName()}, locale);
                }
                redirect.addFlashAttribute("message", message);
            }
        }
        return "redirect:/index.html";
    }
}
