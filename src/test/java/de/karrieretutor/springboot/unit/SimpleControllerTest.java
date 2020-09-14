package de.karrieretutor.springboot.unit;

import de.karrieretutor.springboot.SimpleController;
import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.ProduktRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static de.karrieretutor.springboot.enums.Kategorie.KAT1;
import static de.karrieretutor.springboot.enums.Unterkategorie.KEINE_AHNUNG;
import static java.util.Optional.of;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = SimpleController.class)
public class SimpleControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    ProduktRepository produktRepository;

    @Test
    void kaufen() throws Exception {
        Produkt dummyProdukt = new Produkt("Name", "Herkunft", KAT1, KEINE_AHNUNG.SUBKAT1);
        when(produktRepository.findById(1L)).thenReturn(of(dummyProdukt));


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
    void entfernen_warenkorb_leer() throws Exception {
        mvc.perform(get("/entfernen")
                .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/warenkorb.html"))
                .andExpect(flash().attribute("message", "Produkt mit ID \"1\" nicht im Warenkorb gefunden."));
    }
}