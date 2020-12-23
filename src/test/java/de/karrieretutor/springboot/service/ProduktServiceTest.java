package de.karrieretutor.springboot.service;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.ProduktRepository;
import de.karrieretutor.springboot.enums.Kategorie;
import de.karrieretutor.springboot.enums.Unterkategorie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProduktServiceTest {
    @Mock
    ProduktRepository produktRepository;
    @InjectMocks
    ProduktService produktService;
    final static int MAX_PRODUKTE = 5;

    @BeforeEach
    public void init() {
        given(produktRepository.findAll()).willReturn(createProdukte());
    }

    @Test
    void ladeProdukte() {
        List<Produkt> produkte = produktService.ladeProdukte();
        assertThat(produkte).isNotEmpty();
        assertThat(produkte.size()).isEqualTo(MAX_PRODUKTE);
    }

    @Test
    void getProdukt() {
    }

    @Test
    void updateProdukt() {
    }

    @Test
    void deleteProdukt() {
    }

    @Test
    void findProdukt() {
        Produkt produkt = produktService.findProdukt(1L);
        assertEquals(produkt.getName(), "produkt1");
    }
    @Test
    void findProdukt_falsche_ID() {
        Produkt produkt = produktService.findProdukt(99L);
        assertEquals(produkt, null);
    }
    @Test
    void findProdukt_leere_ID() {
        Produkt produkt = produktService.findProdukt(null);
        assertEquals(produkt, null);
    }

    Iterable<Produkt> createProdukte() {
        List<Produkt> produkte = new ArrayList<>();
        for (int i = 1; i <= MAX_PRODUKTE; i++) {
            int index = i % Kategorie.values().length;
            Kategorie kat = Kategorie.values()[index];
            int subindex = i % Unterkategorie.values().length;
            Unterkategorie subkat = Unterkategorie.values()[subindex];
            Produkt produkt = new Produkt("produkt" + i, "herkunft", kat, subkat, (double) i);
            produkt.setId((long)i);
            produkte.add(produkt);
        }
        return produkte;
    }
}