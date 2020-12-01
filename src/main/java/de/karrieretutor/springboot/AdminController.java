package de.karrieretutor.springboot;

import de.karrieretutor.springboot.model.Produkt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/")
public class AdminController {

    @Autowired
    MessageSource messageSource;


    @GetMapping("bearbeiten.html")
    public String bearbeiten(@RequestParam(required = false, name = "id") Long id, Model model, HttpSession session) {
        Produkt aktuellesProdukt = (id != null) ? getProdukt(session, id) : new Produkt();
        model.addAttribute("produkt", aktuellesProdukt);
        return "admin/bearbeiten";
    }

    private Produkt getProdukt(HttpSession session, Long produktId) {
        List<Produkt> produkte = (List<Produkt>) session.getAttribute("Produkte");
        if (produkte == null)
            return null;
        Produkt produkt = produkte.stream().filter(p -> p.id == produktId).findFirst().orElse(null);
        System.out.println("produkt = " + produkt);
        return produkt;
    }
}
