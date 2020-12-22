package de.karrieretutor.springboot.controller;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.Warenkorb;
import de.karrieretutor.springboot.enums.Kategorie;
import de.karrieretutor.springboot.enums.Unterkategorie;
import de.karrieretutor.springboot.service.ProduktService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static de.karrieretutor.springboot.Const.PRODUCTS;
import static de.karrieretutor.springboot.enums.Kategorie.KAT1;
import static de.karrieretutor.springboot.enums.Unterkategorie.SUBKAT1;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value=SimpleController.class)
public class SimpleControllerTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    private ProduktService produktService;
    @Autowired
    MessageSource messageSource;
    @MockBean
    Warenkorb warenkorb;

    Produkt dummyProdukt = new Produkt("Name", "Herkunft", KAT1, SUBKAT1, 3.99d);

    @Test
    public void shop() throws Exception {
        List<Produkt> produkte = createProdukte();
        when(produktService.ladeProdukte()).thenReturn(produkte);

        mvc.perform(get("/index.html")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(model().attribute(PRODUCTS, produkte));
    }

    @Test
    public void fotos_mit_ID() throws Exception {
        byte[] testBytes = {1,2,3,4,5};
        dummyProdukt.setDatei(testBytes);
        when(produktService.getProdukt(1L)).thenReturn(dummyProdukt);

        mvc.perform(get("/fotos/1"))
                .andExpect(status().is(200))
                .andExpect(content().bytes(testBytes));
    }

    @Test
    public void fotos_nicht_gefunden() throws Exception {
        URL imageURL = this.getClass().getClassLoader().getResource("./static/images/no-image.png");
        byte[] testBytes = Files.readAllBytes(Paths.get(imageURL.toURI()));

        mvc.perform(get("/fotos/1"))
                .andExpect(status().is(200))
                .andExpect(content().bytes(testBytes));
    }

    @Test
    void kaufen() throws Exception {
        when(produktService.getProdukt(1L)).thenReturn(dummyProdukt);

        mvc.perform(get("/kaufen")
                .param("id", "1"))
                .andExpect(flash().attribute("message", "Produkt \"Name\" zum Warenkorb hinzugefügt."));
    }

    @Test
    void kaufen_falsche_ProduktID() throws Exception {
        mvc.perform(get("/kaufen")
                .param("id", "999999"))
                .andExpect(flash().attribute("message", "Produkt mit der ID \"999999\" nicht gefunden."));
    }

    @Test
    void kaufen_leere_ProduktID() throws Exception {
        mvc.perform(get("/kaufen")
                .param("id", ""))
                .andExpect(flash().attribute("message", "Produkt mit der ID \"null\" nicht gefunden."));
    }

    @Test
    void entfernen_mit_ID() throws Exception {
        when(warenkorb.produktEntfernen(1L)).thenReturn(dummyProdukt);

        mvc.perform(get("/entfernen")
                .param("id", "1"))
                .andExpect(flash().attribute("message", "Produkt \"Name\" vom Warenkorb entfernt."));
    }

    @Test
    void entfernen_falsche_ID() throws Exception {
        when(warenkorb.produktEntfernen(1L)).thenReturn(dummyProdukt);

        mvc.perform(get("/entfernen")
                .param("id", "99"))
                .andExpect(flash().attribute("message", "Produkt mit ID \"99\" nicht im Warenkorb gefunden."));
    }

    List<Produkt> createProdukte() {
        List<Produkt> produkte = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            int index = i % Kategorie.values().length;
            Kategorie kat = Kategorie.values()[index];
            int subindex = i % Unterkategorie.values().length;
            Unterkategorie subkat = Unterkategorie.values()[subindex];
            Produkt produkt = new Produkt("produkt" + i, "herkunft", kat, subkat, (double) i);
            produkte.add(produkt);
        }
        return produkte;
    }

}
