package de.karrieretutor.springboot.service;

import de.karrieretutor.springboot.domain.Bestellung;
import de.karrieretutor.springboot.domain.BestellungRepository;
import de.karrieretutor.springboot.domain.Kunde;
import de.karrieretutor.springboot.enums.BestellStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BestellService {
    @Autowired
    BestellungRepository bestellRepository;
    @Autowired
    KundenService kundenService;

    @Transactional(readOnly = true)
    public Bestellung lade(Long id) {
        return this.bestellRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Bestellung> bestellungenVonKunde(Long kundenId) {
        List<Bestellung> bestellungen = bestellRepository.findByKundeId(kundenId);
        if (bestellungen == null)
            bestellungen = new ArrayList<Bestellung>();
        return bestellungen;
    }

    @Transactional()
    public Bestellung speichere(Bestellung bestellung, Boolean istNeu) {
        Kunde kunde = bestellung.getKunde();
        if (istNeu) {
            kundenService.speichern(kunde);
        }
        bestellung.setDatum(LocalDateTime.now());
        bestellung.setStatus(BestellStatus.OFFEN);
        bestellung.setProdukte(bestellung.getProdukte());
        bestellRepository.save(bestellung);
        return bestellung;
    }
    public Bestellung speichere(Bestellung bestellung) {
        return this.speichere(bestellung, false);
    }

    @Transactional()
    public void loesche(Bestellung bestellung) {
        this.bestellRepository.delete(bestellung);
    }

}
