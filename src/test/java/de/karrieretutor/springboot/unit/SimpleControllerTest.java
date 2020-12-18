package de.karrieretutor.springboot.unit;

import de.karrieretutor.springboot.controller.SimpleController;
import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.enums.Kategorie;
import de.karrieretutor.springboot.enums.Unterkategorie;
import de.karrieretutor.springboot.service.KundenService;
import de.karrieretutor.springboot.service.ProduktService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static de.karrieretutor.springboot.enums.Kategorie.KAT1;
import static de.karrieretutor.springboot.enums.Unterkategorie.SUBKAT1;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SimpleController.class)
public class SimpleControllerTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    private ProduktService produktService;
    @MockBean
    KundenService kundenService;
    @Autowired
    MessageSource messageSource;


//    @Test
    // @GetMapping("/index.html")
    public void shop_without_Params() throws Exception {
        when(produktService.ladeProdukte()).thenReturn(createProdukte());

        mvc.perform(get("/index.html")
                .contentType(MediaType.TEXT_HTML));

    }

    @Test
    void kaufen() throws Exception {
        Produkt dummyProdukt = new Produkt("Name", "Herkunft", KAT1, SUBKAT1, 3.99d);
        when(produktService.getProdukt(1L)).thenReturn(dummyProdukt);

        mvc.perform(get("/kaufen")
                .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("index.html"))
                .andExpect(flash().attribute("message", "Produkt \"Name\" zum Warenkorb hinzugefügt."));
    }

    @Test
    void kaufen_falsche_ProduktID() throws Exception {
        mvc.perform(get("/kaufen")
                .param("id", "999999"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("index.html"))
                .andExpect(flash().attribute("message", "Produkt mit der ID \"999999\" nicht gefunden."));
    }

    @Test
    void kaufen_leere_ProduktID() throws Exception {
        mvc.perform(get("/kaufen")
                .param("id", ""))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("index.html"))
                .andExpect(flash().attribute("message", "Produkt mit der ID \"null\" nicht gefunden."));
    }

/*
    @Test
    void entfernen() throws Exception {
        Produkt dummyProdukt = new Produkt("Name", "Herkunft", KAT1, SUBKAT1, 3.99d);
        Warenkorb warenkorb = mock(Warenkorb.class);
        when(warenkorb.produktEntfernen(1L)).thenReturn(dummyProdukt);

        mvc.perform(get("/entfernen")
                .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cart.html"))
                .andExpect(flash().attribute("message", "Produkt \"Name\" vom Warenkorb entfernt."));
    }
*/

    List<Produkt> createProdukte() {
        List<Produkt> produkte = new ArrayList<>();
        for(int i=1; i<= 10; i++) {
            int index = i % Kategorie.values().length;
            Kategorie kat = Kategorie.values()[index];
            int subindex = i % Unterkategorie.values().length;
            Unterkategorie subkat = Unterkategorie.values()[subindex];
            Produkt produkt = new Produkt("produkt"+i, "herkunft", kat, subkat, (double)i);
            produkte.add(produkt);
        }
        return produkte;
    }

}
