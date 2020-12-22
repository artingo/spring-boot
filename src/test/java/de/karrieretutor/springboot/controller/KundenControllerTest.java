package de.karrieretutor.springboot.controller;

import de.karrieretutor.springboot.domain.Warenkorb;
import de.karrieretutor.springboot.dto.Login;
import de.karrieretutor.springboot.service.EmailService;
import de.karrieretutor.springboot.service.KundenService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static de.karrieretutor.springboot.Const.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(KundenController.class)
class KundenControllerTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    KundenService kundenService;
    @MockBean
    EmailService emailService;
    @MockBean
    Warenkorb warenkorb;
    @Autowired
    MessageSource messageSource;

    MockHttpSession session = new MockHttpSession();

    @Before
    public void setUp() {
        session.setAttribute(CART, warenkorb);
    }

    @Test
    void kundenDetails() {


    }

    @Test
    void loginSeite() throws Exception {
        // TODO: warenkorb in die Session
/*
        mvc.perform(get("/kunde/login").session(session)
                .contentType(MediaType.TEXT_HTML))
                .andExpect(model().attribute(LOGIN, new Login()));
*/
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void registrierSeite() {
    }

    @Test
    void registrieren() {
    }

    @Test
    void speichern() {
    }
}