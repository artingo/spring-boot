package de.karrieretutor.springboot.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTest {
    @Autowired
    MockMvc mvc;

    @Test
    void kaufen() throws Exception {
        mvc.perform(get("/kaufen")
                .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("index.html"))
                .andExpect(flash().attribute("message", "Produkt \"Java-Kurs\" zum Warenkorb hinzugefügt."));
    }

    @Test
    void kaufen_fehler() throws Exception {
        mvc.perform(get("/kaufen")
                .param("id", "99999"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("index.html"))
                .andExpect(flash().attribute("message", "Produkt mit der ID \"99999\" nicht gefunden."));
    }

    @Test
    void entfernen() throws Exception {
        mvc.perform(get("/kaufen")
                .param("id", "1"));
        mvc.perform(get("/entfernen")
                .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/warenkorb.html"))
                .andExpect(flash().attribute("message", "Produkt \"Java-Kurs\" vom Warenkorb entfernt."));
    }
}